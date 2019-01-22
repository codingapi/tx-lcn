package com.codingapi.txlcn.manager.core.storage;

import java.util.List;

/**
 * Description: Manager缓存使用规范
 * Date: 19-1-21 下午2:53
 *
 * @author ujued
 */
public interface FastStorage {
    
    
    /*-----------------------以下为事务组相关------------------------------*/
    /**
     * 初始化事务组
     *
     * @param groupId 事务组id
     * @throws FastStorageException fastStorageException
     */
    void initGroup(String groupId) throws FastStorageException;
    
    /**
     * 某个事务组是否存在某个单元信息
     *
     * @param groupId         事务组id
     * @param transactionUnit 事务单元信息
     * @return bool
     */
    boolean containsTransactionUnit(String groupId, TransactionUnit transactionUnit);
    
    /**
     * 是否存在事务组
     *
     * @param groupId 事务组id
     * @return bool
     */
    boolean containsGroup(String groupId);
    
    /**
     * 获取同一事务组的所有单元信息
     *
     * @param groupId 事务组id
     * @return list
     * @throws FastStorageException fastStorageException
     */
    List<TransactionUnit> findTransactionUnitsFromGroup(String groupId) throws FastStorageException;
    
    /**
     * 事务组加入新事物单元
     *
     * @param groupId         事务组id
     * @param transactionUnit 事务单元信息
     * @throws FastStorageException fastStorageException
     */
    void saveTransactionUnitToGroup(String groupId, TransactionUnit transactionUnit) throws FastStorageException;
    
    /**
     * 创建事务组
     *
     * @param groupId 事务组id
     * @throws FastStorageException fastStorageException
     */
    void clearGroup(String groupId) throws FastStorageException;
    
    /**
     * 保存事务状态
     *
     * @param groupId 事务组id
     * @param state   状态 1 commit 0 rollback
     * @throws FastStorageException fastStorageException
     */
    void saveTransactionState(String groupId, int state) throws FastStorageException;
    
    /**
     * 获取事务组状态
     *
     * @param groupId 事务组id
     * @return int
     * @throws FastStorageException fastStorageException
     */
    int getTransactionState(String groupId) throws FastStorageException;
    
    /**
     * 获取锁
     *
     * @param cate cate
     * @param key  key
     * @throws FastStorageException fastStorageException
     */
    void acquireLock(String cate, String key) throws FastStorageException;
    
    /**
     * 释放锁
     *
     * @param cate cate
     * @param key  key
     * @throws FastStorageException fastStorageException
     */
    void releaseLock(String cate, String key) throws FastStorageException;
    
    
    
    /*-----------------------以下为Manager后台token相关------------------------------*/
    /**
     * 保存token
     *
     * @param token token
     * @throws FastStorageException fastStorageException
     */
    void saveToken(String token) throws FastStorageException;
    
    /**
     * 查找所有的token
     *
     * @return list
     * @throws FastStorageException fastStorageException
     */
    List<String> findTokens() throws FastStorageException;
    
    /**
     * 移除token
     *
     * @param token token
     * @throws FastStorageException fastStorageException
     */
    void removeToken(String token) throws FastStorageException;
    
    
    
    /*-----------------------以下为Manager注册管理相关------------------------------*/
    /**
     * 保存Manager地址 ip:port
     *
     * @param address ip:port
     * @throws FastStorageException fastStorageException
     */
    void saveTMAddress(String address) throws FastStorageException;
    
    /**
     * 查询所有Manager地址
     *
     * @return list
     * @throws FastStorageException fastStorageException
     */
    List<String> findTMAddresses() throws FastStorageException;
    
    /**
     * 移除Manager地址
     *
     * @param address ip:port
     * @throws FastStorageException fastStorageException
     */
    void removeTMAddress(String address) throws FastStorageException;
    
}
