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

/**
 * Description: 事务管理器
 * Date: 19-1-9 下午5:50
 *
 * @author ujued
 */
public interface TransactionManager {

    /**
     * 开始分布式事务
     *
     * @param groupId 分布式事务
     * @throws TransactionException TransactionException
     */
    void begin(String groupId) throws TransactionException;

    /**
     * 分布式事务成员加入
     *
     * @param dtxContext       dtxTransaction
     * @param unitId           unitId
     * @param unitType         unitType
     * @param modId            modId
     * @param transactionState transactionState
     * @throws TransactionException TransactionException
     */
    void join(DTXContext dtxContext, String unitId, String unitType, String modId, int transactionState) throws TransactionException;

    /**
     * 提交分布式事务。出错会记录异常记录
     *
     * @param dtxContext transaction
     * @throws TransactionException TransactionException
     */
    void commit(DTXContext dtxContext) throws TransactionException;

    /**
     * 回滚分布式事务。出错会记录异常记录
     *
     * @param dtxContext transaction
     * @throws TransactionException TransactionException
     */
    void rollback(DTXContext dtxContext) throws TransactionException;

    /**
     * 关闭分布式事务。出错会记录异常记录
     *
     * @param groupId groupId
     */
    void close(String groupId);

    /**
     * 获取事务状态（补偿机制）。出错返回-1
     *
     * @param groupId groupId
     * @return transactionState
     */
    int transactionState(String groupId);

    /**
     * 获取事务状态。从FastStorage
     *
     * @param groupId groupId
     * @return transactionState
     */
    int transactionStateFromFastStorage(String groupId);

}
