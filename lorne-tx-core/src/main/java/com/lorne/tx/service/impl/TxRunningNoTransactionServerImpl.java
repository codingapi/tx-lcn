package com.lorne.tx.service.impl;

import com.lorne.core.framework.utils.KidUtils;
import com.lorne.tx.bean.TxTransactionInfo;
import com.lorne.tx.bean.TxTransactionLocal;
//import com.lorne.tx.compensate.model.TransactionRecover;
import com.lorne.tx.service.TransactionServer;
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
        logger.info("tx-no-running-start->" + txGroupId);
        long t1 = System.currentTimeMillis();

//
//        TransactionRecover recover = new TransactionRecover();
//        recover.setId(KidUtils.generateShortUuid());
//        recover.setInvocation(info.getInvocation());
//        recover.setTaskId(kid);
//        recover.setGroupId(txGroupId);

        TxTransactionLocal txTransactionLocal = new TxTransactionLocal();
        txTransactionLocal.setGroupId(txGroupId);
        txTransactionLocal.setHasStart(false);
      //  txTransactionLocal.setRecover(recover);
        txTransactionLocal.setKid(kid);
        txTransactionLocal.setTransactional(info.getTransactional());
        txTransactionLocal.setMaxTimeOut(info.getMaxTimeOut());
        TxTransactionLocal.setCurrent(txTransactionLocal);

        try {
            return point.proceed();
        } catch (Throwable e) {
            throw e;
        } finally {
            TxTransactionLocal.setCurrent(null);
            long t2 = System.currentTimeMillis();
            logger.info("tx-no-running-end->" + txGroupId+",time->"+(t2-t1));
        }
    }

}
