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
package com.codingapi.txlcn.spi.message.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description:
 * Date: 2018/12/20
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TxExceptionParams implements Serializable {

    public static final short NOTIFY_UNIT_ERROR = 0;

    public static final short ASK_ERROR = 1;

    public static final short NOTIFY_GROUP_ERROR = 2;
    public static final Short TXC_ROLLBACK_ERROR = 3;

    private String groupId;

    private String unitId;


    /**
     * 异常情况。-1 【未知】 0 【TxManager通知事务】， 1 【TxClient查询事务状态】 2 【事务发起方通知事务组】
     */
    private Short registrar;

    /**
     * 事务状态 0 回滚 1提交
     */
    private Short transactionState;
}
