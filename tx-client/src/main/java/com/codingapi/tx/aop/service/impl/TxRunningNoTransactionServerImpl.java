package com.codingapi.tx.aop.service.impl;

import com.codingapi.tx.Constants;
import com.codingapi.tx.aop.bean.TxTransactionInfo;
import com.codingapi.tx.aop.bean.TxTransactionLocal;
import com.codingapi.tx.aop.service.TransactionServer;
import com.lorne.core.framework.utils.KidUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 分布式事务启动参与事务中的业务处理（无事务模块）
 * Created by lorne on 2017/6/8.
 */
@Service(value = "txRunningNoTransactionServer")
public class TxRunningNoTransactionServerImpl implements TransactionServer {


    private Logger logger = LoggerFactory.getLogger(TxRunningNoTransactionServerImpl.class);

    @Override
    public Object execute(final ProceedingJoinPoint point, final TxTransactionInfo info) throws Throwable {

        String kid = KidUtils.generateShortUuid();
        String txGroupId = info.getTxGroupId();
        logger.debug("--->begin readonly transaction, groupId: " + txGroupId);
        long t1 = System.currentTimeMillis();


        TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
        txTransactionLocal.setGroupId(txGroupId);
        txTransactionLocal.setHasStart(false);
        txTransactionLocal.setKid(kid);
        txTransactionLocal.setMaxTimeOut(Constants.txServer.getCompensateMaxWaitTime());
        txTransactionLocal.setMode(info.getMode());
        txTransactionLocal.setReadOnly(true);
        TxTransactionLocal.setCurrent(txTransactionLocal);

        try {
            return point.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            TxTransactionLocal.setCurrent(null);
            long t2 = System.currentTimeMillis();
            logger.debug("<---end readonly transaction,groupId:" + txGroupId+",execute time:"+(t2-t1));
        }
    }

}
