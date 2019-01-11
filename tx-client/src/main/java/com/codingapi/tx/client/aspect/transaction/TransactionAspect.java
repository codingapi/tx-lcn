package com.codingapi.tx.client.aspect.transaction;

import com.codingapi.tx.client.bean.DTXInfo;
import com.codingapi.tx.client.config.TxClientConfig;
import com.codingapi.tx.client.support.common.DTXInfoPool;
import com.codingapi.tx.commons.annotation.*;
import com.codingapi.tx.commons.util.DTXFunctions;
import com.codingapi.tx.commons.util.Transactions;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * LCN 事务拦截器
 * create by lorne on 2018/1/5
 */

@Aspect
@Component
@Slf4j
public class TransactionAspect implements Ordered {

    private final TxClientConfig txClientConfig;

    private final AspectBeforeServiceExecutor aspectBeforeServiceExecutor;

    @Autowired
    public TransactionAspect(TxClientConfig txClientConfig, AspectBeforeServiceExecutor aspectBeforeServiceExecutor) {
        this.txClientConfig = txClientConfig;
        this.aspectBeforeServiceExecutor = aspectBeforeServiceExecutor;
    }

    /**
     * 分布式事务切点描述
     */
    @Pointcut("@annotation(com.codingapi.tx.commons.annotation.TxTransaction)")
    public void txTransactionPointcut() {
    }

    /**
     * 分布式事务切点描述 (Type of LCN)
     */
    @Pointcut("@annotation(com.codingapi.tx.commons.annotation.LcnTransaction)")
    public void lcnTransactionPointcut() {
    }

    /**
     * 分布式事务切点描述 (Type of TXC)
     */
    @Pointcut("@annotation(com.codingapi.tx.commons.annotation.TxcTransaction)")
    public void txcTransactionPointcut() {
    }

    /**
     * 分布式事务切点描述 (Type of TCC)
     */
    @Pointcut("@annotation(com.codingapi.tx.commons.annotation.TccTransaction)")
    public void tccTransactionPointcut() {
    }


    @Around("txTransactionPointcut()")
    public Object transactionRunning(ProceedingJoinPoint point) throws Throwable {
        DTXInfo dtxInfo = DTXInfoPool.get(point);
        TxTransaction txTransaction = dtxInfo.getBusinessMethod().getAnnotation(TxTransaction.class);
        dtxInfo.setTransactionType(txTransaction.type());
        dtxInfo.setTransactionFunc(txTransaction.func());
        return aspectBeforeServiceExecutor.runTransaction(dtxInfo, point::proceed);
    }

    @Around("lcnTransactionPointcut() && !txcTransactionPointcut()" +
            "&& !tccTransactionPointcut() && !txTransactionPointcut()")
    public Object runWithLcnTransaction(ProceedingJoinPoint point) throws Throwable {
        DTXInfo dtxInfo = DTXInfoPool.get(point);
        LcnTransaction lcnTransaction = dtxInfo.getBusinessMethod().getAnnotation(LcnTransaction.class);
        dtxInfo.setTransactionType(Transactions.LCN);
        dtxInfo.setTransactionFunc(lcnTransaction.func());
        return aspectBeforeServiceExecutor.runTransaction(dtxInfo, point::proceed);
    }

    @Around("txcTransactionPointcut() && !lcnTransactionPointcut()" +
            "&& !tccTransactionPointcut() && !txTransactionPointcut()")
    public Object runWithTxcTransaction(ProceedingJoinPoint point) throws Throwable {
        DTXInfo dtxInfo = DTXInfoPool.get(point);
        TxcTransaction txcTransaction = dtxInfo.getBusinessMethod().getAnnotation(TxcTransaction.class);
        dtxInfo.setTransactionType(Transactions.TXC);
        dtxInfo.setTransactionFunc(txcTransaction.func());
        return aspectBeforeServiceExecutor.runTransaction(dtxInfo, point::proceed);
    }

    @Around("tccTransactionPointcut() && !lcnTransactionPointcut()" +
            "&& !txcTransactionPointcut() && !txTransactionPointcut()")
    public Object runWithTccTransaction(ProceedingJoinPoint point) throws Throwable {
        DTXInfo dtxInfo = DTXInfoPool.get(point);
        TccTransaction tccTransaction = dtxInfo.getBusinessMethod().getAnnotation(TccTransaction.class);
        dtxInfo.setTransactionType(Transactions.TCC);
        dtxInfo.setTransactionFunc(tccTransaction.func());
        return aspectBeforeServiceExecutor.runTransaction(dtxInfo, point::proceed);
    }

    @Around("this(com.codingapi.tx.commons.annotation.ITxTransaction) && execution( * *(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        if (!(point.getThis() instanceof ITxTransaction)) {
            throw new IllegalStateException("error join point");
        }
        DTXInfo dtxInfo = DTXInfoPool.get(point);
        ITxTransaction txTransaction = (ITxTransaction) point.getThis();
        dtxInfo.setTransactionType(txTransaction.transactionType());
        dtxInfo.setTransactionFunc(DTXFunctions.CREATE_OR_JOIN);
        return aspectBeforeServiceExecutor.runTransaction(dtxInfo, point::proceed);
    }

    @Override
    public int getOrder() {
        return txClientConfig.getControlOrder();
    }

}
