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
package com.codingapi.txlcn.tm.txmsg.transaction;

import com.codingapi.txlcn.common.exception.TransactionException;
import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tm.core.DTXContext;
import com.codingapi.txlcn.tm.core.DTXContextRegistry;
import com.codingapi.txlcn.tm.core.TransactionManager;
import com.codingapi.txlcn.tm.txmsg.RpcExecuteService;
import com.codingapi.txlcn.tm.txmsg.TransactionCmd;
import com.codingapi.txlcn.txmsg.RpcClient;
import com.codingapi.txlcn.txmsg.params.JoinGroupParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author ujued
 */
@Service("rpc_join-group")
public class JoinGroupExecuteService implements RpcExecuteService {

    private static final TxLogger txLogger = TxLogger.newLogger(JoinGroupExecuteService.class);

    private final TransactionManager transactionManager;

    private final DTXContextRegistry dtxContextRegistry;

    private final RpcClient rpcClient;


    @Autowired
    public JoinGroupExecuteService(TransactionManager transactionManager,
                                   DTXContextRegistry dtxContextRegistry, RpcClient rpcClient) {
        this.transactionManager = transactionManager;
        this.dtxContextRegistry = dtxContextRegistry;
        this.rpcClient = rpcClient;
    }


    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            DTXContext dtxContext = dtxContextRegistry.get(transactionCmd.getGroupId());
            JoinGroupParams joinGroupParams = transactionCmd.getMsg().loadBean(JoinGroupParams.class);
            txLogger.txTrace(transactionCmd.getGroupId(), joinGroupParams.getUnitId(), "unit:{} try join group:{}",
                    joinGroupParams.getUnitId(), transactionCmd.getGroupId());
            transactionManager.join(dtxContext, joinGroupParams.getUnitId(), joinGroupParams.getUnitType(),
                    rpcClient.getAppName(transactionCmd.getRemoteKey()), joinGroupParams.getTransactionState());
            txLogger.txTrace(transactionCmd.getGroupId(), joinGroupParams.getUnitId(), "unit:{} joined.",
                    joinGroupParams.getUnitId());
        } catch (TransactionException e) {
            txLogger.error(this.getClass().getSimpleName(), e.getMessage());
            throw new TxManagerException(e.getLocalizedMessage());
        }
        // non response
        return null;
    }
}
