package com.codingapi.txlcn.tm.core.storage;

import com.codingapi.txlcn.commons.exception.FastStorageException;

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
     * init DTX group
     *
     * @param groupId groupId
     * @throws FastStorageException fastStorageException
     */
    void initGroup(String groupId) throws FastStorageException;

    /**
     * DTX group has unit
     *
     * @param groupId         groupId
     * @param transactionUnit transactionUnit
     * @return bool
     */
    boolean containsTransactionUnit(String groupId, TransactionUnit transactionUnit);

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
     * @param address ip:port
     * @throws FastStorageException fastStorageException
     */
    void saveTMAddress(String address) throws FastStorageException;

    /**
     * find all Manager
     *
     * @return list
     * @throws FastStorageException fastStorageException
     */
    List<String> findTMAddresses() throws FastStorageException;

    /**
     * delete Manager address
     *
     * @param address ip:port
     * @throws FastStorageException fastStorageException
     */
    void removeTMAddress(String address) throws FastStorageException;
}
