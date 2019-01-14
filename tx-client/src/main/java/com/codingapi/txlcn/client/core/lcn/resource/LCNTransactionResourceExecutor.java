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
package com.codingapi.txlcn.client.core.lcn.resource;

import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.client.support.resouce.TransactionResourceExecutor;
import com.codingapi.txlcn.client.support.common.cache.TransactionAttachmentCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.function.Supplier;

/**
 * @author lorne
 */
@Service(value = "transaction_lcn")
@Slf4j
public class LCNTransactionResourceExecutor implements TransactionResourceExecutor {


    private final TransactionAttachmentCache transactionAttachmentCache;

    @Autowired
    public LCNTransactionResourceExecutor(TransactionAttachmentCache transactionAttachmentCache) {
        this.transactionAttachmentCache = transactionAttachmentCache;
    }

    @Override
    public Connection proxyConnection(Supplier<Connection> connectionSupplier) throws Throwable {
        String groupId = DTXLocal.cur().getGroupId();
        String unitId = DTXLocal.cur().getUnitId();
        Connection connection = transactionAttachmentCache.attachment(
                groupId, unitId, LCNConnectionProxy.class, () -> new LCNConnectionProxy(connectionSupplier.get())
        );
        connection.setAutoCommit(false);
        return connection;
    }
}
