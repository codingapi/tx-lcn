package com.codingapi.tx.aop.service.impl;

import com.codingapi.tx.Constants;
import com.codingapi.tx.aop.bean.TxCompensateLocal;
import com.codingapi.tx.aop.bean.TxTransactionInfo;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.aop.service.TransactionServer;
import com.codingapi.tx.framework.task.TaskGroupManager;
import com.codingapi.tx.framework.task.TaskState;
import com.codingapi.tx.framework.task.TxTask;
import com.codingapi.tx.netty.service.MQTxManagerService;
import com.lorne.core.framework.utils.KidUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分布式事务启动开始时的业务处理
 * Created by lorne on 2017/6/8.
 */
@Service(value = "txStartTransactionServer")
public class TxStartTransactionServerImpl implements TransactionServer {


    private Logger logger = LoggerFactory.getLogger(TxStartTransactionServerImpl.class);


    @Autowired
    protected MQTxManagerService txManagerService;


    @Override
    public Object execute(ProceedingJoinPoint point,final TxTransactionInfo info) throws Throwable {
        //分布式事务开始执行

        logger.info("事务发起方...");

        logger.debug("--->分布式事务开始执行 begin start transaction");

        final long start = System.currentTimeMillis();

        //标记执行状态，0失败，1成功。
        int state = 0;

        //1：构建事务组id
        final String groupId = TxCompensateLocal.current()==null?KidUtils.generateShortUuid():TxCompensateLocal.current().getGroupId();

        //2：向tx-mange发起事务创建
        logger.debug("创建事务组并发送消息");
        txManagerService.createTransactionGroup(groupId);

        //3：封装事务上下文（封装组id，事务超时时间，事务模式，是否发起方），并设置当前线程中。这里贯穿整个生命周期。
        TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
        txTransactionLocal.setGroupId(groupId);
        txTransactionLocal.setHasStart(true);
        txTransactionLocal.setMaxTimeOut(Constants.txServer.getCompensateMaxWaitTime());
        txTransactionLocal.setMode(info.getTxTransaction().mode());
        txTransactionLocal.setReadOnly(info.getTxTransaction().readOnly());
        TxTransactionLocal.setCurrent(txTransactionLocal);

        try {
            //4：执行业务方法
            Object obj = point.proceed();
            //修改执行结果状态
            state = 1;
            return obj;
        } catch (Throwable e) {
            //5：回滚事务
            state = rollbackException(info,e);
            throw e;
        } finally {

            final String type = txTransactionLocal.getType();
            //6：通知tx-m关闭事务组，进入事务提交第一阶段，tx-m会通知所有参与者提交。获取所有参与者执行后的最终结果。
            int rs = txManagerService.closeTransactionGroup(groupId, state);

            int lastState = rs==-1?0:state;

            int executeConnectionError = 0;

            //获取task，走到这里的时候，发起方若执行了回滚，这里为null。
            final TxTask waitTask = TaskGroupManager.getInstance().getTask(groupId, type);
            if(waitTask!=null){
                //设置最终执行结果
                waitTask.setState(lastState);
                //7：唤醒阻塞任务，这里会执行本地db连接池commit，或者回滚。然后归还db连接。
                waitTask.signalTask();
                //自旋等待线程池删除任务。
                while (!waitTask.isRemove()){
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if(waitTask.getState()== TaskState.connectionError.getCode()){
                    //本地执行失败.
                    executeConnectionError = 1;

                    lastState = 0;
                }
            }

            final TxCompensateLocal compensateLocal =  TxCompensateLocal.current();

            if (compensateLocal == null) {
                long end = System.currentTimeMillis();
                long time = end - start;
                //8：发起方执行失败但是参与方成功，或者发起方成功，参与方失败。则通知tx-M记录补偿
                if ((executeConnectionError == 1&&rs == 1)||(lastState == 1 && rs == 0)) {
                    logger.debug("记录补偿日志");//记录补偿事务数据到tx-m
                    txManagerService.sendCompensateMsg(groupId, time, info,executeConnectionError);
                }
            }else{
                //调用方执行成功
                if(rs==1){
                    //标记提交
                    lastState = 1;
                }else{
                    //标记已回滚。
                    lastState = 0;
                }
            }
            //清除事务上下文信息
            TxTransactionLocal.setCurrent(null);
            logger.debug("<---分布式事务 end start transaction");
            logger.debug("start transaction over, res -> groupId:" + groupId + ", now state:" + (lastState == 1 ? "commit" : "rollback"));

        }
    }


    private int  rollbackException(TxTransactionInfo info,Throwable throwable){

        //spring 事务机制默认回滚异常.
        if(RuntimeException.class.isAssignableFrom(throwable.getClass())){
            return 0;
        }

        if(Error.class.isAssignableFrom(throwable.getClass())){
            return 0;
        }

        //回滚异常检测.
        for(Class<? extends Throwable> rollbackFor:info.getTxTransaction().rollbackFor()){

            //存在关系
            if(rollbackFor.isAssignableFrom(throwable.getClass())){
                return 0;
            }

        }

        //不回滚异常检测.
        for(Class<? extends Throwable> rollbackFor:info.getTxTransaction().noRollbackFor()){

            //存在关系
            if(rollbackFor.isAssignableFrom(throwable.getClass())){
                return 1;
            }
        }
        return 1;
    }
}
