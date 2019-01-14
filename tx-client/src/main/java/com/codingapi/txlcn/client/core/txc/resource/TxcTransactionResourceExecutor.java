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
package com.codingapi.txlcn.client.core.txc.resource;

import com.codingapi.txlcn.client.support.resouce.TransactionResourceExecutor;
import com.codingapi.txlcn.jdbcproxy.p6spy.spring.ConnectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.function.Supplier;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component("transaction_txc")
public class TxcTransactionResourceExecutor implements TransactionResourceExecutor {

    private final ConnectionHelper connectionHelper;

    @Autowired
    public TxcTransactionResourceExecutor(ConnectionHelper connectionHelper) {
        this.connectionHelper = connectionHelper;
    }


    @Override
    public Connection proxyConnection(Supplier<Connection> connectionSupplier) {
        return connectionHelper.proxy(connectionSupplier.get());
    }
}
