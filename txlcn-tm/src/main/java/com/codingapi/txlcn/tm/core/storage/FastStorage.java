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
package com.codingapi.txlcn.tm.core.storage;

import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.tm.cluster.TMProperties;

import java.util.List;
import java.util.Set;

/**
 * Description: Manager cache
 * Date: 19-1-21 下午2:53
 *
 * @author ujued
 */
public interface FastStorage {


    /*-----------------------DTX group------------------------------*/

    /**
     * init DTX group.
     * note: group info should clean by self 10 seconds after DTX time.
     *
     * @param groupId groupId
     * @throws FastStorageException fastStorageException
     */
    void initGroup(String groupId) throws FastStorageException;

    /**
     * has group
     *
     * @param groupId groupId
     * @return bool
     */
    boolean containsGroup(String groupId);

    /**
     * get group all unit
     *
     * @param groupId groupId
     * @return list
     * @throws FastStorageException fastStorageException
     */
    List<TransactionUnit> findTransactionUnitsFromGroup(String groupId) throws FastStorageException;

    /**
     * group join unit
     *
     * @param groupId         groupId
     * @param transactionUnit transactionUnit
     * @throws FastStorageException fastStorageException
     */
    void saveTransactionUnitToGroup(String groupId, TransactionUnit transactionUnit) throws FastStorageException;

    /**
     * clear group
     *
     * @param groupId groupId
     * @throws FastStorageException fastStorageException
     */
    void clearGroup(String groupId) throws FastStorageException;

    /**
     * save DTX state
     * note: transaction state must clean by self 10 seconds after DTX time.
     *
     * @param groupId groupId
     * @param state   status 1 commit 0 rollback
     * @throws FastStorageException fastStorageException
     */
    void saveTransactionState(String groupId, int state) throws FastStorageException;

    /**
     * get DTC state
     *
     * @param groupId groupId
     * @return int
     * @throws FastStorageException fastStorageException
     */
    int getTransactionState(String groupId) throws FastStorageException;

    /**
     * get lock
     * note: lock info should clean by self after DTX time
     *
     * @param contextId contextId
     * @param locks     locks
     * @param lockValue lockValue
     * @throws FastStorageException fastStorageException
     */
    void acquireLocks(String contextId, Set<String> locks, LockValue lockValue) throws FastStorageException;

    /**
     * unlock
     *
     * @param contextId contextId
     * @param locks     locks
     * @throws FastStorageException fastStorageException
     */
    void releaseLocks(String contextId, Set<String> locks) throws FastStorageException;



    /*-----------------------admin token------------------------------*/

    /**
     * save token
     *
     * @param token token
     * @throws FastStorageException fastStorageException
     */
    void saveToken(String token) throws FastStorageException;

    /**
     * find all token
     *
     * @return list
     * @throws FastStorageException fastStorageException
     */
    List<String> findTokens() throws FastStorageException;

    /**
     * delete token
     *
     * @param token token
     * @throws FastStorageException fastStorageException
     */
    void removeToken(String token) throws FastStorageException;



    /*-----------------------Manager------------------------------*/

    /**
     * save Manager address is ip:port
     *
     * @param tmProperties ip:port
     * @throws FastStorageException fastStorageException
     */
    void saveTMProperties(TMProperties tmProperties) throws FastStorageException;

    /**
     * find all Manager
     *
     * @return list
     * @throws FastStorageException fastStorageException
     */
    List<TMProperties> findTMProperties() throws FastStorageException;

    /**
     * delete Manager address
     *
     * @param host            host
     * @param transactionPort transactionPort
     * @throws FastStorageException fastStorageException
     */
    void removeTMProperties(String host, int transactionPort) throws FastStorageException;

    /*-----------------------Machine ID------------------------------*/

    /**
     * 申请或刷新一个MachineID
     *
     * @param machineMaxSize 限制大小
     * @param timeout        超时删除
     * @return MachineID
     * @throws FastStorageException FastStorageException
     */
    long acquireMachineId(long machineMaxSize, long timeout) throws FastStorageException;

    void refreshMachines(long timeout, long... machines);
}
