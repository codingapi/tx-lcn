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

import com.codingapi.txlcn.commons.annotation.DTXPropagation;
import com.codingapi.txlcn.commons.bean.TransactionInfo;
import com.codingapi.txlcn.commons.util.Transactions;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * Description:
 * Date: 19-1-11 下午1:21
 *
 * @author ujued
 */
@AllArgsConstructor
@Data
public class DTXInfo {

    private String transactionType;

    private DTXPropagation transactionPropagation;

    private TransactionInfo transactionInfo;

    /**
     * 用户实例对象的业务方法（包含注解信息）
     */
    private Method businessMethod;

    private String unitId;

    public DTXInfo(Method method, Object[] args, Class<?> targetClass) {
        this.transactionInfo = new TransactionInfo();
        this.transactionInfo.setTargetClazz(targetClass);
        this.transactionInfo.setArgumentValues(args);
        this.transactionInfo.setMethod(method.getName());
        this.transactionInfo.setMethodStr(method.toString());
        this.transactionInfo.setParameterTypes(method.getParameterTypes());

        this.businessMethod = method;
        this.unitId = Transactions.unitId(method.toString());
    }

    public void reanalyseMethodArgs(Object[] args) {
        this.transactionInfo.setArgumentValues(args);
    }

}