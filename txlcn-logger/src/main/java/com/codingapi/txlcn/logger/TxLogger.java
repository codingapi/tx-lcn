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
package com.codingapi.txlcn.logger;

import com.codingapi.txlcn.common.util.Transactions;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/26
 *
 * @author codingapi
 */
public interface TxLogger {

    static TxLogger newLogger(Class<?> type) {
        return new DefaultTxLogger(type);
    }

    /**
     * trace log
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @param tag     tag
     * @param content content
     * @param args    args
     */
    void trace(String groupId, String unitId, String tag, String content, Object... args);

    /**
     * 事务日志
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @param content content
     * @param args    args
     */
    default void txTrace(String groupId, String unitId, String content, Object... args) {
        trace(groupId, unitId, Transactions.TAG_TRANSACTION, content, args);
    }

    /**
     * 任务日志
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @param content content
     * @param args    args
     */
    default void taskTrace(String groupId, String unitId, String content, Object... args) {
        trace(groupId, unitId, Transactions.TAG_TASK, content, args);
    }

    void error(String groupId, String unitId, String tag, String content, Object... args);

    default void error(String tag, String content, Object... args) {
        error("non", "non", tag, content, args);
    }
}
