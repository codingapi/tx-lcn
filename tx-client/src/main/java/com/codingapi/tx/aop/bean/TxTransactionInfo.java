package com.codingapi.tx.aop.bean;

import com.codingapi.tx.annotation.TxTransaction;
import com.codingapi.tx.annotation.TxTransactionMode;
import com.codingapi.tx.model.TransactionInvocation;


/**
 * 切面控制对象
 * Created by lorne on 2017/6/8.
 */
public class TxTransactionInfo {


    private TxTransaction transaction;


    private TxTransactionLocal txTransactionLocal;

    /**
     * 事务组Id
     */
    private String txGroupId;


    private TransactionInvocation invocation;

    private TxTransactionMode mode;

    public TxTransactionInfo(TxTransaction transaction, TxTransactionLocal txTransactionLocal, TransactionInvocation invocation, String txGroupId) {
        this.transaction = transaction;
        this.txTransactionLocal = txTransactionLocal;
        this.txGroupId = txGroupId;
        this.invocation = invocation;
    }

    public TxTransactionMode getMode() {
        return mode;
    }

    public void setMode(TxTransactionMode mode) {
        this.mode = mode;
    }

    public TxTransaction getTransaction() {
        return transaction;
    }

    public TxTransactionLocal getTxTransactionLocal() {
        return txTransactionLocal;
    }

    public String getTxGroupId() {
        return txGroupId;
    }

    public TransactionInvocation getInvocation() {
        return invocation;
    }

}
