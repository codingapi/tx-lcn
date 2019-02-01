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

    /**
     * trace log
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @param tag     tag
     * @param content content
     */
    void trace(String groupId, String unitId, String tag, String content, Object... args);

    /**
     * 事务消息
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @param content content
     * @param args    args
     */
    default void transactionInfo(String groupId, String unitId, String content, Object... args) {
        info(groupId, unitId, Transactions.TAG_TRANSACTION, content, args);
    }

    /**
     * info log. todo
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @param tag     tag
     * @param content content
     */
    default void info(String groupId, String unitId, String tag, String content, Object... args) {
        trace(groupId, unitId, tag, content, args);
    }

    /**
     * error log. todo
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @param content content
     */
    default void error(String groupId, String unitId, String content, Object... args) {
        trace(groupId, unitId, Transactions.TE, content, args);
    }

    default void error(String groupId, String unitId, String tag, String content, Object... args) {
        trace(groupId, unitId, tag, content, args);
    }

    default void error(String tag, String content, Object... args) {
        error("", "", tag, content, args);
    }

}
