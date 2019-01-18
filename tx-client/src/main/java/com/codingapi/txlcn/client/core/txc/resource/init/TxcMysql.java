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

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
public class TxcMysql implements TxcSql {

    @Override
    public String lockTableSql() {
        return "CREATE TABLE `txc_lock`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `table_name` varchar(64) NULL DEFAULT NULL,\n" +
                "  `key_value` varchar(32) NULL DEFAULT NULL,\n" +
                "  `group_id` varchar(32) NULL DEFAULT NULL,\n" +
                "  `unit_id` varchar(32) NULL DEFAULT NULL,\n" +
                "  `x_lock` int(11) NULL DEFAULT NULL,\n" +
                "  `s_lock` int(11) NULL DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`) USING BTREE,\n" +
                "  UNIQUE INDEX `table_name`(`table_name`, `key_value`, `x_lock`) USING BTREE\n" +
                ")";
    }

    @Override
    public String undoLogTableSql() {
        return "CREATE TABLE `txc_undo_log`  (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `gmt_create` bigint(20) NULL DEFAULT NULL,\n" +
                "  `gmt_modified` bigint(20) NULL DEFAULT NULL,\n" +
                "  `group_id` varchar(32) NULL DEFAULT NULL,\n" +
                "  `unit_id` varchar(32) NULL DEFAULT NULL,\n" +
                "  `rollback_info` longblob NULL DEFAULT NULL,\n" +
                "  `status` int(11) NULL DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ")";
    }

    @Override
    public String lockTableName() {
        return "txc_lock";
    }

    @Override
    public String undoLogTableName() {
        return "txc_undo_log";
    }
}
