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
package com.codingapi.txlcn.client.bean;


import com.codingapi.txlcn.client.aspect.BusinessCallback;
import com.codingapi.txlcn.commons.annotation.DTXPropagation;
import com.codingapi.txlcn.commons.bean.TransactionInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * 切面控制对象
 * Created by lorne on 2017/6/8.
 */
@Data
@AllArgsConstructor
public class TxTransactionInfo {

    private String transactionType;

    /**
     * 事务发起方
     */
    private boolean transactionStart;

    /**
     * 事务组标识
     */
    private String groupId;

    /**
     * 事务单元标识
     */
    private String unitId;

    /**
     * 事务切面信息
     */
    private TransactionInfo transactionInfo;

    /**
     * 业务执行器
     */
    private BusinessCallback businessCallback;

    /**
     * 切点方法
     */
    private Method pointMethod;

    /**
     * 事务单元职责
     */
    private DTXPropagation propagation;

}

