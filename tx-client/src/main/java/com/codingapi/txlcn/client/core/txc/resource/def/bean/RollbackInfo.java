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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 回滚信息
 * Date: 2018/12/13
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RollbackInfo implements Serializable {

    /**
     * 回滚Sql语句列表
     */
    private List<StatementInfo> rollbackSqlList = new ArrayList<>();

    /**
     * 回滚信息状态。-1时表示不应用回滚信息
     * 此时可能是资源被锁定时失败，并未对资源做任何操作
     */
    private transient int status;
}
