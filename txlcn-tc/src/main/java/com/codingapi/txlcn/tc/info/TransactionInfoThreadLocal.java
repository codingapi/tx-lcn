package com.codingapi.txlcn.tc.info;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
public class TransactionInfoThreadLocal {

    private final static ThreadLocal<TransactionInfo> threadLocal = new ThreadLocal<>();

    public static TransactionInfo current(){
        return threadLocal.get();
    }

    public static void push(TransactionInfo transactionInfo){
        threadLocal.set(transactionInfo);
    }


}
