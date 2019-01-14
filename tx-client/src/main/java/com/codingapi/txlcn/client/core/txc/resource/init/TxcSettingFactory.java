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
package com.codingapi.txlcn.client.core.txc.resource.init;

import com.codingapi.txlcn.client.core.txc.resource.util.SqlUtils;

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
public interface TxcSettingFactory {

    /**
     * 允许TXC事务模式
     *
     * @return enable
     */
    default boolean enable() {
        return true;
    }

    /**
     * 锁表名称
     *
     * @return lockTableName
     */
    default String lockTableName() {
        return SqlUtils.LOCK_TABLE;
    }

    /**
     * 撤销SQL信息表名
     *
     * @return undoLogTableName
     */
    default String undoLogTableName() {
        return SqlUtils.UNDO_LOG_TABLE;
    }

    /**
     * 事务锁表创建SQL
     *
     * @return lockTableSql
     */
    String lockTableSql();

    /**
     * 撤销日志表创建SQL
     *
     * @return undoLogTableSql
     */
    String undoLogTableSql();
}
