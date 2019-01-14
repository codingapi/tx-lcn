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
package com.codingapi.txlcn.client.aspect.weave;

import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.client.support.LCNTransactionBeanHelper;
import com.codingapi.txlcn.client.support.resouce.TransactionResourceExecutor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Objects;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/2
 *
 * @author lorne
 */
@Component
@Slf4j
public class DTXResourceWeaver {

    private final LCNTransactionBeanHelper transactionBeanHelper;

    @Autowired
    public DTXResourceWeaver(LCNTransactionBeanHelper transactionBeanHelper) {
        this.transactionBeanHelper = transactionBeanHelper;
    }

    public Object around(ProceedingJoinPoint point) throws Throwable {
        DTXLocal dtxLocal = DTXLocal.cur();
        if (Objects.nonNull(dtxLocal) && dtxLocal.isProxy()) {
            String transactionType = dtxLocal.getTransactionType();
            TransactionResourceExecutor transactionResourceExecutor = transactionBeanHelper.loadTransactionResourceExecuter(transactionType);
            Connection connection = transactionResourceExecutor.proxyConnection(() -> {
                try {
                    return (Connection) point.proceed();
                } catch (Throwable throwable) {
                    throw new IllegalStateException(throwable);
                }
            });
            log.info("proxy a sql connection: {}.", connection);
            return connection;
        }
        return point.proceed();
    }
}
