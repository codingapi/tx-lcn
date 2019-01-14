package com.codingapi.tx.manager.core.context;

import org.springframework.stereotype.Component;

/**
 * Description: Transaction只存有GroupId, 目前Redis事务组信息只存groupId
 * 这里没有跟Redis交互, 考虑到后续功能增加, 上下文会由Redis缓存担任
 * Date: 1/11/19
 *
 * @author ujued
 */
@Component
public class DTXTransactionContext {

    public DTXTransaction newContext(String groupId) {
        return getTransaction(groupId);
    }

    public DTXTransaction getTransaction(String groupId) {
        return new SimpleDTXTransaction(groupId);
    }

    void destroyTransaction(String groupId) {
        // noting to do
    }
}
