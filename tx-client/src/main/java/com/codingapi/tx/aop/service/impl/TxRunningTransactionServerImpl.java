package com.codingapi.tx.aop.service.impl;

import com.codingapi.tx.Constants;
import com.codingapi.tx.aop.bean.TxTransactionInfo;
import com.codingapi.tx.model.TxGroup;
import com.lorne.core.framework.exception.ServiceException;
import com.lorne.core.framework.utils.KidUtils;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.datasource.ILCNTransactionControl;
import com.codingapi.tx.framework.task.TaskGroupManager;
import com.codingapi.tx.framework.task.TxTask;
import com.codingapi.tx.netty.service.MQTxManagerService;
import com.codingapi.tx.aop.service.TransactionServer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 分布式事务启动参与事务中的业务处理
 * Created by lorne on 2017/6/8.
 */
@Service(value = "txRunningTransactionServer")
public class TxRunningTransactionServerImpl implements TransactionServer {

    @Autowired
    private MQTxManagerService txManagerService;

    @Autowired
    private ILCNTransactionControl transactionControl;

    private Logger logger = LoggerFactory.getLogger(TxRunningTransactionServerImpl.class);

    @Override
    public Object execute(final ProceedingJoinPoint point, final TxTransactionInfo info) throws Throwable {

        logger.info("事务参与方...");
        //1：构建任务id
        String kid = KidUtils.generateShortUuid();
        String txGroupId = info.getTxGroupId();
        logger.debug("--->begin running transaction,groupId:" + txGroupId);
        long t1 = System.currentTimeMillis();
        //是否是同一个事务下，第一次进入默认不在。
        boolean isHasIsGroup =  transactionControl.hasGroup(txGroupId);

        //2：构建上下文对象。
        TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
        txTransactionLocal.setGroupId(txGroupId);
        txTransactionLocal.setHasStart(false);
        txTransactionLocal.setKid(kid);
        txTransactionLocal.setHasIsGroup(isHasIsGroup);
        txTransactionLocal.setMaxTimeOut(Constants.txServer.getCompensateMaxWaitTime());
        txTransactionLocal.setMode(info.getMode());
        TxTransactionLocal.setCurrent(txTransactionLocal);

        try {
            //3：执行业务方法
            Object res = point.proceed();

            //4：针对有db操作的处理。
            if(!txTransactionLocal.isReadOnly()) {
                //获取该注解的业务方法名
                String methodStr = info.getInvocation().getMethodStr();
                //添加事务组子对象
                TxGroup resTxGroup = txManagerService.addTransactionGroup(txGroupId, kid, isHasIsGroup, methodStr);

                //已经进入过该模块的，不再执行此方法
                if(!isHasIsGroup) {
                    String type = txTransactionLocal.getType();
                    //获取任务，该任务在创建db连接时创建。
                    TxTask waitTask = TaskGroupManager.getInstance().getTask(kid, type);

                    //lcn 连接已经开始等待时.自旋等待任务被删除
                    while (waitTask != null && !waitTask.isAwait()) {
                        TimeUnit.MILLISECONDS.sleep(1);
                    }

                    //tx-m没有发现该事务组，则回滚。
                    if (resTxGroup == null) {
                        //通知业务回滚事务
                        if (waitTask != null) {
                            //修改事务组状态异常
                            waitTask.setState(-1);
                            //唤醒db连接进行回滚。
                            waitTask.signalTask();
                            throw new ServiceException("update TxGroup error, groupId:" + txGroupId);
                        }
                    }
                }
            }

            return res;
        } catch (Throwable e) {
            //二种情况进入这里
            //第一种：业务方法执行失败，这里会首先调用回滚方法，然后进入这里。
            //第二种：当 point.proceed() 业务代码中 db事务正常提交，开始等待，后续处理发生异常。
            //由于没有加入事务组，不会收到通知。这里唤醒并回滚
            if(!isHasIsGroup) {
                String type = txTransactionLocal.getType();
                TxTask waitTask = TaskGroupManager.getInstance().getTask(kid, type);
                // 有一定几率不能唤醒: wait的代码是在另一个线程，有可能线程还没执行到wait，先执行到了这里
                // TODO 要不要 sleep 1毫秒
                logger.warn("wake the waitTask: {}", (waitTask != null && waitTask.isAwait()));
                if (waitTask != null && waitTask.isAwait()) {
                    waitTask.setState(-1);
                    waitTask.signalTask();
                }
            }
            throw e;
        } finally {
            //清空上下文信息
            TxTransactionLocal.setCurrent(null);
            long t2 = System.currentTimeMillis();
            logger.debug("<---end running transaction,groupId:" + txGroupId+",execute time:"+(t2-t1));

        }
    }

}
