package com.codingapi.tx.aop.service.impl;

import com.codingapi.tx.Constants;
import com.codingapi.tx.aop.bean.TxCompensateLocal;
import com.codingapi.tx.aop.bean.TxTransactionInfo;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.aop.service.TransactionServer;
import com.codingapi.tx.framework.task.TaskGroupManager;
import com.codingapi.tx.framework.task.TaskState;
import com.codingapi.tx.framework.task.TxTask;
import com.codingapi.tx.framework.thread.HookRunnable;
import com.codingapi.tx.model.TxGroup;
import com.codingapi.tx.netty.service.MQTxManagerService;
import com.lorne.core.framework.exception.ServiceException;
import com.lorne.core.framework.utils.KidUtils;
import com.lorne.core.framework.utils.task.ConditionUtils;
import com.lorne.core.framework.utils.task.Task;
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

        logger.info("--->begin start transaction");

        final long start = System.currentTimeMillis();

        int state = 0;

        //创建事务组
        TxGroup txGroup = txManagerService.createTransactionGroup();

        //获取不到模块信息重新连接，本次事务异常返回数据.
        if (txGroup == null) {
            throw new ServiceException("create TxGroup error");
        }

        final String groupId = txGroup.getGroupId();

        TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
        txTransactionLocal.setGroupId(groupId);
        txTransactionLocal.setHasStart(true);
        txTransactionLocal.setMaxTimeOut(Constants.maxOutTime);
        TxTransactionLocal.setCurrent(txTransactionLocal);

        try {
            Object obj = point.proceed();
            state = 1;
            return obj;
        } catch (Throwable e) {
            state = rollbackException(info,e);
            throw e;
        } finally {

            final int resState = state;
            final String type = txTransactionLocal.getType();

            //确保返回数据之前，业务已经都执行完毕.
            final Task task = ConditionUtils.getInstance().createTask(KidUtils.getKid());

            final TxCompensateLocal compensateLocal =  TxCompensateLocal.current();

            //hook 保护确保下面的代码可以正常执行，当遇到挂机的情况时也会执行完下面代码
            new Thread(new HookRunnable() {
                @Override
                public void run0() {

                    while (!task.isAwait() && !Thread.currentThread().interrupted()) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int rs = txManagerService.closeTransactionGroup(groupId, resState);

                    int lastState = rs==-1?0:resState;

                    int executeConnectionError = 0;

                    //控制本地事务的数据提交
                    final TxTask waitTask = TaskGroupManager.getInstance().getTask(groupId, type);
                    if(waitTask!=null){
                        waitTask.setState(lastState);
                        waitTask.signalTask();

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

                    if (compensateLocal == null) {
                        long end = System.currentTimeMillis();
                        long time = end - start;
                        if (executeConnectionError == 1||(lastState == 1 && rs == 0)) {
                            //记录补偿日志
                            txManagerService.sendCompensateMsg(groupId, time, info,executeConnectionError);
                        }
                    }

                    task.setState(lastState);
                    task.signalTask();
                }

            }).start();

            task.awaitTask();
            int lastState =task.getState();
            task.remove();

            TxTransactionLocal.setCurrent(null);
            logger.info("<---end start transaction");
            logger.info("start transaction over, res -> groupId:" + groupId + ", now state:" + (lastState == 1 ? "commit" : "rollback"));

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
        for(Class<? extends Throwable> rollbackFor:info.getTransaction().rollbackFor()){

            //存在关系
            if(rollbackFor.isAssignableFrom(throwable.getClass())){
                return 0;
            }

        }

        //不回滚异常检测.
        for(Class<? extends Throwable> rollbackFor:info.getTransaction().noRollbackFor()){

            //存在关系
            if(rollbackFor.isAssignableFrom(throwable.getClass())){
                return 1;
            }
        }
        return 1;
    }
}
