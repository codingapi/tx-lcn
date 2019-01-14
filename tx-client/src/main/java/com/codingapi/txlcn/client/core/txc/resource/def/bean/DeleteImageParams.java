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
package com.codingapi.txlcn.client.core.txc.resource.def.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/14
 *
 * @author ujued
 */
@NoArgsConstructor
@Data
public class DeleteImageParams {
    private String groupId;
    private String unitId;
    private RollbackInfo rollbackInfo;
    private List<String> primaryKeys;
    private List<String> columns;
    private List<String> tables;
    private String sqlWhere;

    public DeleteImageParams setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
        return this;
    }

    public DeleteImageParams setColumns(List<String> columns) {
        this.columns = columns;
        return this;
    }

    public DeleteImageParams setTables(List<String> tables) {
        this.tables = tables;
        return this;
    }

    public DeleteImageParams setSqlWhere(String sqlWhere) {
        this.sqlWhere = sqlWhere;
        return this;
    }

    public DeleteImageParams setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public DeleteImageParams setUnitId(String unitId) {
        this.unitId = unitId;
        return this;
    }

    public DeleteImageParams setRollbackInfo(RollbackInfo rollbackInfo) {
        this.rollbackInfo = rollbackInfo;
        return this;
    }
}
