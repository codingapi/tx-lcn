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

import com.codingapi.txlcn.client.bean.DTXInfo;
import com.codingapi.txlcn.client.support.common.DTXInfoPool;
import com.codingapi.txlcn.commons.annotation.*;
import com.codingapi.txlcn.commons.util.Transactions;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Objects;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/13
 *
 * @author codingapi
 */
class InterceptorInvocationUtils {

    static DTXInfo load(MethodInvocation invocation){
        TxTransaction txTransaction = invocation.getMethod().getAnnotation(TxTransaction.class);
        String transactionType = Transactions.LCN;
        DTXPropagation dtxPropagation = DTXPropagation.REQUIRED;

        if (Objects.nonNull(txTransaction)) {
            transactionType = txTransaction.type();
            dtxPropagation = txTransaction.dtxp();
        } else {
            LcnTransaction lcnTransaction = invocation.getMethod().getAnnotation(LcnTransaction.class);
            if (Objects.nonNull(lcnTransaction)) {
                transactionType = Transactions.LCN;
                dtxPropagation = lcnTransaction.dtxp();
            } else {
                TxcTransaction txcTransaction = invocation.getMethod().getAnnotation(TxcTransaction.class);
                if (Objects.nonNull(txcTransaction)) {
                    transactionType = Transactions.TXC;
                    dtxPropagation = txcTransaction.dtxp();
                } else {
                    TccTransaction tccTransaction = invocation.getMethod().getAnnotation(TccTransaction.class);
                    if (Objects.nonNull(tccTransaction)) {
                        transactionType = Transactions.TCC;
                        dtxPropagation = tccTransaction.dtxp();
                    }
                }
            }
        }
        DTXInfo dtxInfo = DTXInfoPool.get(invocation);
        dtxInfo.setTransactionType(transactionType);
        dtxInfo.setTransactionPropagation(dtxPropagation);
        return dtxInfo;
    }
}
