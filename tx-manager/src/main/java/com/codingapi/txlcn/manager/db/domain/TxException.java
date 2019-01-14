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
package com.codingapi.txlcn.manager.db.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TxException {

    private Long id;

    /**
     * 事务组ID
     */
    private String groupId;

    /**
     * 事务单元ID
     */
    private String unitId;

    /**
     * 资源管理服务地址
     */
    private String modId;

    /**
     * 事务状态
     */
    private Short transactionState;

    /**
     * 上报方 0 TxManager 1 TxClient
     */
    private short registrar;

    /**
     * 异常状态 0 待处理 1已处理
     */
    private short exState;

    /**
     * 发生时间
     */
    private Date createTime;
}
