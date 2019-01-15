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
package com.codingapi.txlcn.manager.core.context;

import com.codingapi.txlcn.commons.exception.TransactionException;
import com.codingapi.txlcn.manager.core.group.TransactionUnit;

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
     * @param dtxTransaction 分布式事务
     * @throws   TransactionException TransactionException
     */
    void begin(DTXTransaction dtxTransaction) throws TransactionException;

    /**
     * 分布式事务成员加入
     *
     * @param dtxTransaction dtxTransaction
     * @param transactionUnit transactionUnit
     * @throws TransactionException TransactionException
     */
    void join(DTXTransaction dtxTransaction, TransactionUnit transactionUnit) throws TransactionException;

    /**
     * 提交分布式事务。出错会记录异常记录
     *
     * @param transaction transaction
     */
    void commit(DTXTransaction transaction);

    /**
     * 回滚分布式事务。出错会记录异常记录
     *
     * @param transaction transaction
     */
    void rollback(DTXTransaction transaction);

    /**
     * 关闭分布式事务。出错会记录异常记录
     *
     * @param groupTransaction groupTransaction
     */
    void close(DTXTransaction groupTransaction);

    /**
     * 获取事务状态（补偿机制）。出错返回-1
     *
     * @param groupTransaction groupTransaction
     * @return transactionState
     */
    int transactionState(DTXTransaction groupTransaction);
}
