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

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 某表某行记录对应的字段信息，包括主键列表和普通字段列表
 * Date: 2018/12/13
 *
 * @author ujued
 */
@NoArgsConstructor
@Data
public class FieldCluster {
    /**
     * 普通字段相关信息
     */
    private List<FieldValue> fields = new ArrayList<>();

    /**
     * 主键相关信息
     */
    private List<FieldValue> primaryKeys = new ArrayList<>();
}
