package com.codingapi.tx.client.aspect.transaction;

import com.codingapi.tx.client.config.TxClientConfig;
import com.codingapi.tx.commons.annotation.ITxTransaction;
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

    @Pointcut("@annotation(com.codingapi.tx.commons.annotation.TxTransaction)")
    public void txTransactionPointcut() {
    }

    @Pointcut("@annotation(com.codingapi.tx.commons.annotation.LcnTransaction)")
    public void lcnTransactionPointcut() {
    }

    @Pointcut("@annotation(com.codingapi.tx.commons.annotation.TxcTransaction)")
    public void txcTransactionPointcut() {
    }

    @Pointcut("@annotation(com.codingapi.tx.commons.annotation.TccTransaction)")
    public void tccTransactionPointcut() {
    }

    @Around("txTransactionPointcut()")
    public Object transactionRunning(ProceedingJoinPoint point) throws Throwable {
        return aspectBeforeServiceExecutor.runTransaction(null, point);
    }

    @Around("lcnTransactionPointcut() && !txcTransactionPointcut()" +
            "&& !tccTransactionPointcut() && !txTransactionPointcut()")
    public Object runWithLcnTransaction(ProceedingJoinPoint point) throws Throwable {
        return aspectBeforeServiceExecutor.runTransaction(Transactions.LCN, point);
    }

    @Around("txcTransactionPointcut() && !lcnTransactionPointcut()" +
            "&& !tccTransactionPointcut() && !txTransactionPointcut()")
    public Object runWithTxcTransaction(ProceedingJoinPoint point) throws Throwable {
        return aspectBeforeServiceExecutor.runTransaction(Transactions.TXC, point);
    }

    @Around("tccTransactionPointcut() && !lcnTransactionPointcut()" +
            "&& !txcTransactionPointcut() && !txTransactionPointcut()")
    public Object runWithTccTransaction(ProceedingJoinPoint point) throws Throwable {
        return aspectBeforeServiceExecutor.runTransaction(Transactions.TCC, point);
    }

    @Around("this(com.codingapi.tx.commons.annotation.ITxTransaction) && execution( * *(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        if (!(point.getThis() instanceof ITxTransaction)) {
            throw new IllegalStateException("error join point");
        }
        ITxTransaction txTransaction = (ITxTransaction) point.getThis();
        return aspectBeforeServiceExecutor.runTransaction(txTransaction.transactionType(), point);
    }


    @Override
    public int getOrder() {
        return txClientConfig.getControlOrder();
    }

}
