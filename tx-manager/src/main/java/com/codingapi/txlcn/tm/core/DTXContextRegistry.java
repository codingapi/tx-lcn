package com.codingapi.txlcn.tm.core;

import com.codingapi.txlcn.commons.exception.TransactionException;

/**
 * Description: 事务组生命期管理
 * Date: 19-1-21 下午2:51
 *
 * @author ujued
 */
public interface DTXContextRegistry {
    
    /**
     * 创建事务组上下文
     *
     * @param groupId groupId
     * @return DTXContext
     * @throws TransactionException TransactionException
     */
    DTXContext create(String groupId) throws TransactionException;
    
    /**
     * 获取事务组上下文
     *
     * @param groupId groupId
     * @return DTXContext
     * @throws TransactionException TransactionException
     */
    DTXContext get(String groupId) throws TransactionException;
    
    /**
     * 销毁上下文
     *
     * @param groupId groupId
     */
    void destroyContext(String groupId);
}
