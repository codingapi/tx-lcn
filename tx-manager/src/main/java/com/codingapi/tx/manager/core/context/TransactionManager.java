package com.codingapi.tx.manager.core.context;

import com.codingapi.tx.commons.exception.TransactionException;
import com.codingapi.tx.manager.core.group.TransactionUnit;

/**
 * Description: 事务管理器
 * Date: 19-1-9 下午5:50
 *
 * @author ujued
 */
public interface TransactionManager {

    /**
     * 开始分布式事务
     *
     * @param dtxTransaction 分布式事务
     */
    void begin(DTXTransaction dtxTransaction) throws TransactionException;

    /**
     * 分布式事务成员加入
     *
     * @param dtxTransaction
     * @param transactionUnit
     * @throws TransactionException
     */
    void join(DTXTransaction dtxTransaction, TransactionUnit transactionUnit) throws TransactionException;

    /**
     * 提交分布式事务。出错会记录异常记录
     *
     * @param transaction
     */
    void commit(DTXTransaction transaction);

    /**
     * 回滚分布式事务。出错会记录异常记录
     *
     * @param transaction
     */
    void rollback(DTXTransaction transaction);

    /**
     * 关闭分布式事务。出错会记录异常记录
     *
     * @param groupTransaction
     */
    void close(DTXTransaction groupTransaction);

    /**
     * 获取事务状态（补偿机制）。出错返回-1
     *
     * @param groupTransaction
     * @return
     */
    int transactionState(DTXTransaction groupTransaction);
}
