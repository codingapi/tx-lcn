package com.codingapi.txlcn.manager.core;


import com.codingapi.txlcn.commons.exception.TransactionException;
import com.codingapi.txlcn.manager.core.storage.TransactionUnit;

import java.util.List;

/**
 * Description:
 * Date: 19-1-21 下午2:46
 *
 * @author ujued
 */
public interface DTXContext {

    void join(TransactionUnit transactionUnit) throws TransactionException;

    void resetTransactionState(int state) throws TransactionException;

    List<TransactionUnit> transactionUnits() throws TransactionException;

    String groupId();

    int transactionState();
}
