package com.codingapi.txlcn.tc.info;

import com.codingapi.maven.uml.annotation.Model;

/**
 * 事务信息ThreadLocal
 * @author lorne 2020-0305
 */
@Model(flag = "C",value = "事务信息ThreadLocal",color = "#FF88EE")
class TransactionInfoThreadLocal {

    final static ThreadLocal<TransactionInfo> threadLocal = new ThreadLocal<>();

    static TransactionInfo current(){
        return threadLocal.get();
    }

    static void push(TransactionInfo transactionInfo){
        threadLocal.set(transactionInfo);
    }


}
