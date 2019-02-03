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
package com.codingapi.txlcn.tc.core.transaction.txc.analy.def;

import java.util.List;
import java.util.Map;

/**
 * Description: 关系型数据库表与主键的映射关系
 * Date: 19-1-25 下午3:43
 *
 * @author ujued
 */
public interface PrimaryKeysProvider {

    /**
     * 提供TXC模式下所有操作的表的主键，数据库指定主键可以不用在这里体现
     * {@code key} 是数据表名，{@code value} 是数据表对应的主键列表
     *
     * @return primary key list mapped by table name
     */
    Map<String, List<String>> provide();
}
