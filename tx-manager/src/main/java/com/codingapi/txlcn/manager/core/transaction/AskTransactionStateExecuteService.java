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
package com.codingapi.txlcn.manager.core.transaction;

import com.codingapi.txlcn.commons.exception.TransactionException;
import com.codingapi.txlcn.manager.core.DTXContextRegistry;
import com.codingapi.txlcn.manager.core.TransactionManager;
import com.codingapi.txlcn.manager.core.message.RpcExecuteService;
import com.codingapi.txlcn.manager.core.message.TransactionCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
@Component("rpc_ask-transaction-state")
@Slf4j
public class AskTransactionStateExecuteService implements RpcExecuteService {

    private final TransactionManager transactionManager;

    private final DTXContextRegistry dtxContextRegistry;

    @Autowired
    public AskTransactionStateExecuteService(TransactionManager transactionManager, DTXContextRegistry dtxContextRegistry) {
        this.transactionManager = transactionManager;
        this.dtxContextRegistry = dtxContextRegistry;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) {
        try {
            int state = transactionManager.transactionState(dtxContextRegistry.get(transactionCmd.getGroupId()));
            return state == -1 ? 0 : state;
        } catch (TransactionException e) {
            return 0;
        }
    }
}
