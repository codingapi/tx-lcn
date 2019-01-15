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

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 业务操作数据库所受影响的数据库记录
 * Date: 2018/12/13
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModifiedRecord {
    /**
     * 表与字段集合的映射关系
     * {@code key} 表示记录包含的表，{@code value} 表对应受影响的字段
     */
    private Map<String, FieldCluster> fieldClusters = new HashMap<>();
}
