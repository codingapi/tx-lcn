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
package com.codingapi.txlcn.tc.aspect.weave;

import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.support.TxLcnBeanHelper;
import com.codingapi.txlcn.tc.support.resouce.TransactionResourceProxy;
import lombok.extern.slf4j.Slf4j;
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

    private final TxLcnBeanHelper txLcnBeanHelper;

    public DTXResourceWeaver(TxLcnBeanHelper txLcnBeanHelper) {
        this.txLcnBeanHelper = txLcnBeanHelper;
    }

    public Object getConnection(ConnectionCallback connectionCallback) throws Throwable {
        DTXLocalContext dtxLocalContext = DTXLocalContext.cur();
        if (Objects.nonNull(dtxLocalContext) && dtxLocalContext.isProxy()) {
            String transactionType = dtxLocalContext.getTransactionType();
            TransactionResourceProxy resourceProxy = txLcnBeanHelper.loadTransactionResourceProxy(transactionType);
            Connection connection = resourceProxy.proxyConnection(connectionCallback);
            log.debug("proxy a sql connection: {}.", connection);
            return connection;
        }
        return connectionCallback.call();
    }
}
