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

/**
 * Description: 行锁信息
 * Date: 2018/12/13
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LockInfo {
    private String groupId;

    private String unitId;

    /**
     * 数据表
     */
    private String tableName;
    /**
     * 数据表内行标识
     */
    private String keyValue;

    /**
     * 是否是排他锁。为{@code false}时标识此锁为共享锁
     */
    private boolean xLock;

    public LockInfo setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public LockInfo setUnitId(String unitId) {
        this.unitId = unitId;
        return this;
    }

    public LockInfo setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public LockInfo setKeyValue(String keyValue) {
        this.keyValue = keyValue;
        return this;
    }

    public LockInfo setxLock(boolean xLock) {
        this.xLock = xLock;
        return this;
    }
}
