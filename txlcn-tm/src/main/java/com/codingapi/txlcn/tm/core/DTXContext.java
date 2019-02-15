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
import com.codingapi.txlcn.tm.core.storage.GroupProps;
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
     * 获取该事务组事务单元列表。从FastStorage获取。
     *
     * @return list
     * @throws TransactionException TransactionException
     */
    List<TransactionUnit> transactionUnits() throws TransactionException;

    /**
     * 获取事务组ID
     *
     * @return groupId
     */
    String getGroupId();

    /**
     * 获取事务组状态
     *
     * @return int
     */
    int transactionState();
}
