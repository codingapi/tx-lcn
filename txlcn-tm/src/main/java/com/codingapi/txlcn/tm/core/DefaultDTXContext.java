/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.tm.core;

import com.codingapi.txlcn.common.exception.TransactionException;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.tm.core.storage.GroupProps;
import com.codingapi.txlcn.tm.core.storage.TransactionUnit;

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
            fastStorage.saveTransactionUnitToGroup(groupId, transactionUnit);
        } catch (FastStorageException e) {
            throw new TransactionException("attempts to join the non-existent transaction group. ["
                    + transactionUnit.getUnitId() + '@' + transactionUnit.getModId() + ']');
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
    public String getGroupId() {
        return this.groupId;
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
