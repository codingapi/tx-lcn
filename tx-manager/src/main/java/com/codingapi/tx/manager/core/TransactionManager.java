package com.codingapi.tx.manager.core;

import com.codingapi.tx.manager.core.ex.TransactionException;

/**
 * Description:
 * Date: 19-1-11 下午5:50
 *
 * @author ujued
 */
public interface TransactionManager {

    void begin(GroupTransaction dtxTransaction);

    void join(GroupTransaction dtxTransaction, TransactionUnit transactionUnit) throws TransactionException;

    void commit(GroupTransaction transaction);

    void rollback(GroupTransaction transaction);

    void close(GroupTransaction groupTransaction);
}
