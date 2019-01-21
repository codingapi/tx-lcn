package com.codingapi.txlcn.manager.core;

import com.codingapi.txlcn.commons.exception.TransactionException;
import com.codingapi.txlcn.manager.core.storage.FastStorage;
import com.codingapi.txlcn.manager.core.storage.FastStorageException;
import com.codingapi.txlcn.manager.core.storage.TransactionUnit;

import java.util.List;

/**
 * Description:
 * Date: 19-1-21 下午2:53
 *
 * @author ujued
 */
public class DefaultDTXContext implements DTXContext {

    private final FastStorage fastStorage;

    private final String groupId;

    DefaultDTXContext(String groupId, FastStorage fastStorage) {
        this.fastStorage = fastStorage;
        this.groupId = groupId;
    }

    @Override
    public void join(TransactionUnit transactionUnit) throws TransactionException {
        try {
            // idempotent processing
            if (fastStorage.containsTransactionUnit(groupId, transactionUnit)) {
                return;
            }
            fastStorage.saveTransactionUnitToGroup(groupId, transactionUnit);
        } catch (FastStorageException e) {
            throw new TransactionException("attempts to join the non-existent transaction group.");
        }
    }

    @Override
    public void resetTransactionState(int state) throws TransactionException {
        try {
            fastStorage.saveTransactionState(groupId, state);
        } catch (FastStorageException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public List<TransactionUnit> transactionUnits() throws TransactionException {
        try {
            return fastStorage.findTransactionUnitsFromGroup(groupId);
        } catch (FastStorageException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public String groupId() {
        return groupId;
    }

    @Override
    public int transactionState() {
        try {
            return fastStorage.getTransactionState(groupId);
        } catch (FastStorageException e) {
            return -1;
        }
    }
}
