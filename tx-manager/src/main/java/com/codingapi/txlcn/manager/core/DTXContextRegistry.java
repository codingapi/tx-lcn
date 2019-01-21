package com.codingapi.txlcn.manager.core;

import com.codingapi.txlcn.commons.exception.TransactionException;

/**
 * Description:
 * Date: 19-1-21 下午2:51
 *
 * @author ujued
 */
public interface DTXContextRegistry {

    DTXContext create(String groupId) throws TransactionException;

    DTXContext get(String groupId) throws TransactionException;

    void destroyContext(String groupId);
}
