package com.codingapi.tx.client.aspect.transaction;

import com.codingapi.tx.client.bean.DTXInfo;
import com.codingapi.tx.client.support.common.DTXInfoPool;
import com.codingapi.tx.commons.annotation.LcnTransaction;
import com.codingapi.tx.commons.annotation.TccTransaction;
import com.codingapi.tx.commons.annotation.TxTransaction;
import com.codingapi.tx.commons.annotation.TxcTransaction;
import com.codingapi.tx.commons.util.Transactions;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Objects;

/**
 * Description:
 * Date: 1/12/19
 *
 * @author ujued
 */
public class DTXInterceptor extends TransactionInterceptor {

    private final DTXServiceExecutor DTXServiceExecutor;

    public DTXInterceptor(DTXServiceExecutor DTXServiceExecutor) {
        this.DTXServiceExecutor = DTXServiceExecutor;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        DTXInfo dtxInfo = DTXInfoPool.get(invocation);

        TxTransaction txTransaction = dtxInfo.getBusinessMethod().getAnnotation(TxTransaction.class);
        LcnTransaction lcnTransaction = dtxInfo.getBusinessMethod().getAnnotation(LcnTransaction.class);
        TxcTransaction txcTransaction = dtxInfo.getBusinessMethod().getAnnotation(TxcTransaction.class);
        TccTransaction tccTransaction = dtxInfo.getBusinessMethod().getAnnotation(TccTransaction.class);

        if (Objects.nonNull(txTransaction)) {
            dtxInfo.setTransactionType(txTransaction.type());
            dtxInfo.setTransactionPropagation(txTransaction.dtxp());

            return DTXServiceExecutor.runTransaction(dtxInfo, () -> super.invoke(invocation));
        } else if (Objects.nonNull(lcnTransaction)) {
            dtxInfo.setTransactionType(Transactions.LCN);
            dtxInfo.setTransactionPropagation(lcnTransaction.dtxp());
            return DTXServiceExecutor.runTransaction(dtxInfo, () -> super.invoke(invocation));
        } else if (Objects.nonNull(txcTransaction)) {
            dtxInfo.setTransactionType(Transactions.TXC);
            dtxInfo.setTransactionPropagation(txcTransaction.dtxp());
            return DTXServiceExecutor.runTransaction(dtxInfo, () -> super.invoke(invocation));
        } else if (Objects.nonNull(tccTransaction)) {
            dtxInfo.setTransactionType(Transactions.TCC);
            dtxInfo.setTransactionPropagation(tccTransaction.dtxp());
            return DTXServiceExecutor.runTransaction(dtxInfo, () -> super.invoke(invocation));
        }
        return super.invoke(invocation);
    }
}
