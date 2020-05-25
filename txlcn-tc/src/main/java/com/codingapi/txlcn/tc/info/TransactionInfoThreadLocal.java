package com.codingapi.txlcn.tc.info;

/**
 * 事务信息ThreadLocal
 * @author lorne 2020-0305
 */
class TransactionInfoThreadLocal {

    final static ThreadLocal<TransactionInfo> threadLocal = new ThreadLocal<>();

    static TransactionInfo current(){
        return threadLocal.get();
    }

    static void push(TransactionInfo transactionInfo){
        threadLocal.set(transactionInfo);
    }


}
