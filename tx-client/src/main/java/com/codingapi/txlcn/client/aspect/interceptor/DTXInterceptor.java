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
package com.codingapi.txlcn.client.aspect.interceptor;

import com.codingapi.txlcn.client.aspect.weave.DTXLogicWeaver;
import com.codingapi.txlcn.client.bean.DTXInfo;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * Description:
 * Date: 1/12/19
 *
 * @author ujued
 */

public class DTXInterceptor extends TransactionInterceptor {

    private final DTXLogicWeaver dtxLogicWeaver;

    public DTXInterceptor(DTXLogicWeaver dtxLogicWeaver) {
        this.dtxLogicWeaver = dtxLogicWeaver;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        DTXInfo dtxInfo = InterceptorInvocationUtils.load(invocation);
        return dtxLogicWeaver.runTransaction(dtxInfo, () -> super.invoke(invocation));
    }

}
