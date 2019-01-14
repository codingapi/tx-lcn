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
package com.codingapi.txlcn.manager.support.restapi.model;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description:
 * Date: 2018/12/20
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExceptionInfo {

    private long id;

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
     * 异常情况。-1 【未知】 0 【TxManager通知事务】， 1 【TxClient查询事务状态】 2 【事务发起方通知事务组】
     */
    private int registrar;

    /**
     * 异常状态 0 待处理 1已处理
     */
    private short exState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 事务信息
     */
    private JSONObject transactionInfo;
}
