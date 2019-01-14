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
package com.codingapi.txlcn.client.core.txc.resource.def.config;

import com.codingapi.txlcn.client.core.txc.resource.util.SqlUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/14
 *
 * @author codingapi
 */
@Data
@ConfigurationProperties(prefix = "tx-lcn.client.txc")
@Component
public class TxcConfig {

    private int minIdle = 10;

    private boolean enable = true;

    private String lockTableName = SqlUtils.LOCK_TABLE;

    private String undoLogTableName = SqlUtils.UNDO_LOG_TABLE;

    private String lockTableSql = "CREATE TABLE `" + lockTableName + "`  (\n" +
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

    private  String undoLogTableSql = "CREATE TABLE `" + undoLogTableName + "`  (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `gmt_create` bigint(20) NULL DEFAULT NULL,\n" +
            "  `gmt_modified` bigint(20) NULL DEFAULT NULL,\n" +
            "  `group_id` varchar(32) NULL DEFAULT NULL,\n" +
            "  `unit_id` varchar(32) NULL DEFAULT NULL,\n" +
            "  `rollback_info` blob NULL DEFAULT NULL,\n" +
            "  `status` int(11) NULL DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`) USING BTREE\n" +
            ")";


}
