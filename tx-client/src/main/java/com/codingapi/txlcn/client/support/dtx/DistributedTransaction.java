package com.codingapi.txlcn.client.support.dtx;

/**
 * Description:
 * Date: 19-1-16 下午4:20
 *
 * @author ujued
 */
public interface DistributedTransaction {

    void rollback();

    /**
     * 事务状态
     *
     * @return 1 成功 0 失败
     */
    int transactionState();

    void setTransactionState(int state);
}
