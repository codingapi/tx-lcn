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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateImageParams {
    private String groupId;
    private String unitId;
    private RollbackInfo rollbackInfo;
    private List<String> tables;
    private List<String> columns;
    private List<String> primaryKeys;
    private String whereSql;

    public UpdateImageParams setTables(List<String> tables) {
        this.tables = tables;
        return this;
    }

    public UpdateImageParams setColumns(List<String> columns) {
        this.columns = columns;
        return this;
    }

    public UpdateImageParams setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
        return this;
    }

    public UpdateImageParams setWhereSql(String whereSql) {
        this.whereSql = whereSql;
        return this;
    }

    public UpdateImageParams setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public UpdateImageParams setRollbackInfo(RollbackInfo rollbackInfo) {
        this.rollbackInfo = rollbackInfo;
        return this;
    }

    public UpdateImageParams setUnitId(String unitId) {
        this.unitId = unitId;
        return this;
    }
}
