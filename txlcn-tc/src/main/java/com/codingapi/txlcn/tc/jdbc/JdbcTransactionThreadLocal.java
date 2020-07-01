package com.codingapi.txlcn.tc.jdbc;

/**
 * 事务信息ThreadLocal
 * @author lorne 2020-0305
 */
class JdbcTransactionThreadLocal {

    final static ThreadLocal<JdbcTransaction> threadLocal = new ThreadLocal<>();

    static JdbcTransaction current(){
        return threadLocal.get();
    }

    static void push(JdbcTransaction jdbcTransaction){
        threadLocal.set(jdbcTransaction);
    }


}
