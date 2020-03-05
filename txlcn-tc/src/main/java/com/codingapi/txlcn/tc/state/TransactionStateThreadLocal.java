package com.codingapi.txlcn.tc.state;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
public class TransactionStateThreadLocal {

    private final static ThreadLocal<TransactionState> threadLocal = new ThreadLocal<>();

    public static TransactionState current(){
        return threadLocal.get();
    }

    public static void push(TransactionState transactionState){
        threadLocal.set(transactionState);
    }


}
