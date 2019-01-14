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
 * Description: SQL语句对象
 * Date: 2018/12/14
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatementInfo {

    /**
     * 带问号占位符的SQL语句
     */
    private String sql;

    /**
     * SQL占位符处的实际参数
     */
    private Object[] params;
}
