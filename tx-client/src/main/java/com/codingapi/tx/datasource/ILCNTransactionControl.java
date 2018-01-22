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
     * 有无执行过事务操作
     * @return true 有，false 否
     */
    boolean executeTransactionOperation();


    /**
     * 是否没有事务操作  default false
     * @return true 是 false 否
     */
    boolean isNoTransactionOperation();

    /**
     * 设置开启没有事务操作
     */
    void autoNoTransactionOperation();
}
