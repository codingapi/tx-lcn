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
package com.codingapi.txlcn.tc.core.transaction.txc.analy.def.bean;

import lombok.Data;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Date: 19-1-18 下午5:38
 *
 * @author ujued
 */
@Data
public class InsertImageParams {

    /**
     * Auto Increment PK Values
     */
    private Statement statement;

    /**
     * Table Name
     */
    private String tableName;

    private List<Map<String, Object>> primaryKeyValuesList;

    private List<String> fullyQualifiedPrimaryKeys;
}
