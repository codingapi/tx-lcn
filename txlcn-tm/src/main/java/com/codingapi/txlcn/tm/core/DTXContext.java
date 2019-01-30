package com.codingapi.txlcn.tm.core;


import com.codingapi.txlcn.common.exception.TransactionException;
import com.codingapi.txlcn.tm.core.storage.TransactionUnit;

import java.util.List;

/**
 * Description: 事务上下文
 * Date: 19-1-21 下午2:46
 *
 * @author ujued
 */
public interface DTXContext {
    
    /**
     * 加入当前事务组
     *
     * @param transactionUnit transactionUnit
     * @throws TransactionException TransactionException
     */
    void join(TransactionUnit transactionUnit) throws TransactionException;
    
    /**
     * 设置事务组状态
     *
     * @param state state
     * @throws TransactionException TransactionException
     */
    void resetTransactionState(int state) throws TransactionException;
    
    /**
     * 获取该事务组事务单元列表
     *
     * @return list
     * @throws TransactionException TransactionException
     */
    List<TransactionUnit> transactionUnits() throws TransactionException;
    
    /**
     * 获取事务组id
     *
     * @return string
     */
    String groupId();
    
    /**
     * 获取事务组状态
     *
     * @return int
     */
    int transactionState();
}
