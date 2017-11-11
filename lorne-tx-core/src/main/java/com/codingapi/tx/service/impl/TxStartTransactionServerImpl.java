package com.codingapi.tx.service.impl;

import com.codingapi.tx.bean.TxTransactionInfo;
import com.codingapi.tx.mq.model.TxGroup;
import com.lorne.core.framework.exception.ServiceException;
import com.codingapi.tx.Constants;
import com.codingapi.tx.bean.TxTransactionLocal;
import com.codingapi.tx.mq.service.MQTxManagerService;
import com.codingapi.tx.service.TransactionServer;
import com.codingapi.tx.thread.HookRunnable;
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
    public Object execute(ProceedingJoinPoint point, final TxTransactionInfo info) throws Throwable {
        //分布式事务开始执行
        logger.info("tx-start");

        long start = System.currentTimeMillis();
        //创建事务组
        TxGroup txGroup = txManagerService.createTransactionGroup();

        //获取不到模块信息重新连接，本次事务异常返回数据.
        if (txGroup == null) {
            throw new ServiceException("创建事务组异常.");
        }
        final String groupId = txGroup.getGroupId();
        int state = 0;
        try {
            TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
            txTransactionLocal.setGroupId(groupId);
            txTransactionLocal.setHasStart(true);
            txTransactionLocal.setTransactional(info.getTransactional());
            txTransactionLocal.setMaxTimeOut(Constants.maxOutTime);
            TxTransactionLocal.setCurrent(txTransactionLocal);
            Object obj = point.proceed();
            state = 1;
            return obj;
        } catch (Throwable e) {
            throw e;
        } finally {
            int rs  = txManagerService.closeTransactionGroup(groupId, state);
            long end = System.currentTimeMillis();

            final long time = end - start;

            if(state == 1&&rs == 0){
                new Thread(new HookRunnable() {
                    @Override
                    public void run0() {
                        //事务执行日志 info.getInvocation();
                        //todo 记录补偿日志
                        txManagerService.sendCompensateMsg(groupId,time,info);

                    }
                }).start();

            }
            TxTransactionLocal.setCurrent(null);
            logger.info("tx-end-"+txGroup.getGroupId()+">"+state);
        }
    }

}
