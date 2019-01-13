package com.codingapi.tx.client.aspect;

import com.codingapi.tx.client.aspect.weave.DTXLogicWeaver;
import com.codingapi.tx.client.bean.DTXInfo;
import com.codingapi.tx.client.support.common.DTXInfoPool;
import com.codingapi.tx.commons.annotation.*;
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

    private final DTXLogicWeaver dtxLogicWeaver;

    private String transactionType = Transactions.LCN;

    private DTXPropagation dtxPropagation = DTXPropagation.REQUIRED;

    public DTXInterceptor(DTXLogicWeaver dtxLogicWeaver) {
        this.dtxLogicWeaver = dtxLogicWeaver;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TxTransaction txTransaction = invocation.getMethod().getAnnotation(TxTransaction.class);
        if (Objects.nonNull(txTransaction)) {
            this.transactionType = txTransaction.type();
            this.dtxPropagation = txTransaction.dtxp();
        } else {
            LcnTransaction lcnTransaction = invocation.getMethod().getAnnotation(LcnTransaction.class);
            if (Objects.nonNull(lcnTransaction)) {
                this.transactionType = Transactions.LCN;
                this.dtxPropagation = lcnTransaction.dtxp();
            } else {
                TxcTransaction txcTransaction = invocation.getMethod().getAnnotation(TxcTransaction.class);
                if (Objects.nonNull(txcTransaction)) {
                    this.transactionType = Transactions.TXC;
                    this.dtxPropagation = txcTransaction.dtxp();
                } else {
                    TccTransaction tccTransaction = invocation.getMethod().getAnnotation(TccTransaction.class);
                    if (Objects.nonNull(tccTransaction)) {
                        this.transactionType = Transactions.TCC;
                        this.dtxPropagation = tccTransaction.dtxp();
                    }
                }
            }
        }

        DTXInfo dtxInfo = DTXInfoPool.get(invocation);
        dtxInfo.setTransactionType(this.transactionType);
        dtxInfo.setTransactionPropagation(this.dtxPropagation);
        return dtxLogicWeaver.runTransaction(dtxInfo, () -> super.invoke(invocation));
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public DTXPropagation getDtxPropagation() {
        return dtxPropagation;
    }

    public void setDtxPropagation(DTXPropagation dtxPropagation) {
        this.dtxPropagation = dtxPropagation;
    }
}
