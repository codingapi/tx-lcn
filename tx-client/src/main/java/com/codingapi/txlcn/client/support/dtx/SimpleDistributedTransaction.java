package com.codingapi.txlcn.client.support.dtx;

import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * Description:
 * Date: 19-1-16 下午4:21
 *
 * @author ujued
 */
public class SimpleDistributedTransaction implements DistributedTransaction {

    private int state;

    @Override
    public void rollback() {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    @Override
    public int transactionState() {
        return state;
    }

    @Override
    public void setTransactionState(int state) {
        this.state = state;
    }
}
