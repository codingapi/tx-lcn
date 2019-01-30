package com.codingapi.txlcn.tm.core;

import com.codingapi.txlcn.common.exception.TransactionException;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.common.exception.FastStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 19-1-21 下午2:59
 *
 * @author ujued
 */
@Component
public class DefaultDTXContextRegistry implements DTXContextRegistry {

    private final FastStorage fastStorage;

    @Autowired
    public DefaultDTXContextRegistry(FastStorage fastStorage) {
        this.fastStorage = fastStorage;
    }

    @Override
    public DTXContext create(String groupId) throws TransactionException {
        try {
            fastStorage.initGroup(groupId);
        } catch (FastStorageException e) {
            // idempotent processing
            if (e.getCode() != FastStorageException.EX_CODE_REPEAT_GROUP) {
                throw new TransactionException(e);
            }
        }
        return get(groupId);
    }

    @Override
    public DTXContext get(String groupId) throws TransactionException {
        // test has group
        if (!fastStorage.containsGroup(groupId)) {
            throw new TransactionException("non this transaction group.");
        }
        return new DefaultDTXContext(groupId, fastStorage);
    }

    @Override
    public void destroyContext(String groupId) {
        try {
            fastStorage.clearGroup(groupId);
        } catch (FastStorageException e) {
            // idempotent processing
            if (e.getCode() == FastStorageException.EX_CODE_NON_GROUP) {
                return;
            }
            try {
                fastStorage.clearGroup(groupId);
            } catch (FastStorageException e1) {
                throw new IllegalStateException(e1);
            }
        }
    }

    @Override
    public int transactionState(String groupId) {
        try {
            return fastStorage.getTransactionState(groupId);
        } catch (FastStorageException e) {
            return -1;
        }
    }
}
