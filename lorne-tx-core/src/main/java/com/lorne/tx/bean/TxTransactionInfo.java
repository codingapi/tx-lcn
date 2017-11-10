package com.lorne.tx.bean;

import com.lorne.tx.annotation.TxTransaction;
//import com.lorne.tx.compensate.model.TransactionInvocation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

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


   // private TxTransactionCompensate compensate;


//    private TransactionInvocation invocation;


    public TxTransactionInfo(TxTransaction transaction,Transactional transactional, TxTransactionLocal txTransactionLocal, String txGroupId, int maxTimeOut) {
        this.transaction = transaction;
        this.txTransactionLocal = txTransactionLocal;
        this.txGroupId = txGroupId;
        this.maxTimeOut = maxTimeOut;
       // this.compensate = compensate;
//        this.invocation = invocation;
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

//    public TxTransactionCompensate getCompensate() {
//        return compensate;
//    }

//    public TransactionInvocation getInvocation() {
//        return invocation;
//    }

    public Transactional getTransactional() {
        return transactional;
    }
}
