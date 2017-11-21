package com.codingapi.tx.datasource;


/**
 * LCN 代理事务协调控制
 * create by lorne on 2017/9/6
 */
public interface ILCNTransactionControl {

    /**
     * 是否是同一个事务下
     * @param group 事务组id
     * @return  true是，false否
     */
    boolean hasGroup(String group);

    /**
     * 是否是 事务操作
     * @return true是，false否
     */
    boolean hasTransaction();
}
