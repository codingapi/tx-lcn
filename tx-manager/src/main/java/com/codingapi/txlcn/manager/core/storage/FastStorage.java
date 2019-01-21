package com.codingapi.txlcn.manager.core.storage;

import java.util.List;

/**
 * Description:
 * Date: 19-1-21 下午2:53
 *
 * @author ujued
 */
public interface FastStorage {

    void initGroup(String groupId) throws FastStorageException;

    boolean containsTransactionUnit(String groupId, TransactionUnit transactionUnit);

    boolean containsGroup(String groupId);

    List<TransactionUnit> findTransactionUnitsFromGroup(String groupId) throws FastStorageException;

    void saveTransactionUnitToGroup(String groupId, TransactionUnit transactionUnit) throws FastStorageException;

    void clearGroup(String groupId) throws FastStorageException;

    void saveTransactionState(String groupId, int state) throws FastStorageException;

    int getTransactionState(String groupId) throws FastStorageException;


    void acquireLock(String cate, String key) throws FastStorageException;

    void releaseLock(String cate, String key) throws FastStorageException;


    void saveToken(String token) throws FastStorageException;

    List<String> findTokens() throws FastStorageException;

    void removeToken(String token) throws FastStorageException;


    void saveTMAddress(String address) throws FastStorageException;

    List<String> findTMAddresses() throws FastStorageException;

    void removeTMAddress(String address) throws FastStorageException;

}
