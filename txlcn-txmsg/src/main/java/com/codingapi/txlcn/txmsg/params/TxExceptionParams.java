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
package com.codingapi.txlcn.txmsg.params;

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

    /**
     * 未知错误
     */
    public static final short UNKNOWN_ERROR = -1;

    /**
     * 通知事务单元失败
     */
    public static final short NOTIFY_UNIT_ERROR = 0;

    /**
     * 询问事务状态失败
     */
    public static final short ASK_ERROR = 1;

    /**
     * 通知事务组失败（TC Starter）
     */
    public static final short NOTIFY_GROUP_ERROR = 2;

    /**
     * TCC 清理事务失败
     */
    public static final short TCC_CLEAN_ERROR = 3;

    /**
     * TXC 撤销日志失败
     */
    public static final Short TXC_UNDO_ERROR = 4;


    private String groupId;

    private String unitId;

    /**
     * 异常情况
     */
    private Short registrar;

    /**
     * 事务状态 0 回滚 1提交
     */
    private Integer transactionState;

    /**
     * 备注
     */
    private String remark;
}
