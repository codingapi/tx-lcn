package com.codingapi.tx.bean;

import com.codingapi.tx.annotation.TxTransaction;
import com.codingapi.tx.service.model.TransactionInvocation;
import org.springframework.transaction.annotation.Transactional;


/**
 * 切面控制对象
 * Created by lorne on 2017/6/8.
 */
public class TxTransactionInfo {


    private TxTransaction transaction;

    private Transactional transactional;

    private TxTransactionLocal txTransactionLocal;

    private String txGroupId;

    private int maxTimeOut;

    private TransactionInvocation invocation;


    public TxTransactionInfo(TxTransaction transaction,Transactional transactional, TxTransactionLocal txTransactionLocal,TransactionInvocation invocation, String txGroupId, int maxTimeOut) {
        this.transaction = transaction;
        this.txTransactionLocal = txTransactionLocal;
        this.txGroupId = txGroupId;
        this.maxTimeOut = maxTimeOut;
        this.invocation = invocation;
        this.transactional = transactional;
    }

    public int getMaxTimeOut() {
        return maxTimeOut;
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

    public Transactional getTransactional() {
        return transactional;
    }
}
