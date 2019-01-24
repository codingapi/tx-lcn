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
package com.codingapi.txlcn.tm.core;

import com.codingapi.txlcn.commons.exception.TransactionException;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.params.NotifyUnitParams;
import com.codingapi.txlcn.spi.message.util.MessageUtils;
import com.codingapi.txlcn.tm.cluster.ClusterMessenger;
import com.codingapi.txlcn.tm.core.message.MessageCreator;
import com.codingapi.txlcn.tm.core.message.RpcExceptionHandler;
import com.codingapi.txlcn.tm.core.storage.TransactionUnit;
import com.codingapi.txlcn.tm.support.service.TxExceptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Description: 默认事务管理器
 * Date: 19-1-9 下午5:57
 *
 * @author ujued
 */
@Slf4j
@Component
public class SimpleTransactionManager implements TransactionManager {

    private final RpcExceptionHandler rpcExceptionHandler;

    private final RpcClient rpcClient;

    private final TxLogger txLogger;

    private final TxExceptionService exceptionService;

    private final DTXContextRegistry dtxContextRegistry;

    private final ClusterMessenger clusterMessenger;

    @Autowired
    public SimpleTransactionManager(RpcExceptionHandler rpcExceptionHandler,
                                    RpcClient rpcClient, TxLogger txLogger,
                                    TxExceptionService exceptionService, DTXContextRegistry dtxContextRegistry,
                                    ClusterMessenger clusterMessenger) {
        this.rpcExceptionHandler = rpcExceptionHandler;
        this.exceptionService = exceptionService;
        this.rpcClient = rpcClient;
        this.txLogger = txLogger;
        this.dtxContextRegistry = dtxContextRegistry;
        this.clusterMessenger = clusterMessenger;
    }

    @Override
    public void begin(String groupId) throws TransactionException {
        try {
            dtxContextRegistry.create(groupId);
        } catch (TransactionException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public void join(DTXContext dtxContext, String unitId, String unitType, String modId, int userState) throws TransactionException {
        log.debug("unit:{} joined group:{}", unitId, dtxContext.groupId());
        //手动回滚时设置状态为回滚状态 0
        if (userState == 0) {
            dtxContext.resetTransactionState(0);
        }
        TransactionUnit transactionUnit = new TransactionUnit();
        transactionUnit.setModId(modId);
        transactionUnit.setUnitId(unitId);
        transactionUnit.setUnitType(unitType);
        dtxContext.join(transactionUnit);
    }

    @Override
    public void commit(DTXContext dtxContext) throws TransactionException {
        notifyTransaction(dtxContext, 1);
    }

    @Override
    public void rollback(DTXContext dtxContext) throws TransactionException {
        notifyTransaction(dtxContext, 0);
    }

    @Override
    public void close(String groupId) {
        dtxContextRegistry.destroyContext(groupId);
    }

    @Override
    public int transactionState(String groupId) {
        int state = exceptionService.transactionState(groupId);
        //存在数据时返回数据状态
        if (state != -1) {
            return state;
        }
        return dtxContextRegistry.transactionState(groupId);
    }

    private void notifyTransaction(DTXContext dtxContext, int transactionState) throws TransactionException {
        for (TransactionUnit transUnit : dtxContext.transactionUnits()) {
            NotifyUnitParams notifyUnitParams = new NotifyUnitParams();
            notifyUnitParams.setGroupId(dtxContext.groupId());
            notifyUnitParams.setUnitId(transUnit.getUnitId());
            notifyUnitParams.setUnitType(transUnit.getUnitType());
            notifyUnitParams.setState(transactionState);
            log.debug("notify {}'s unit: {}", transUnit.getModId(), transUnit.getUnitId());
            txLogger.trace(dtxContext.groupId(), notifyUnitParams.getUnitId(), Transactions.TAG_TRANSACTION, "notify unit");
            try {
                String transactionUnitModRpcKey;
                List<String> modChannelKeys = rpcClient.remoteKeys(transUnit.getModId());
                if (modChannelKeys.isEmpty()) {
                    transactionUnitModRpcKey = clusterMessenger.tmRpcKeyByModId(transUnit.getModId());
                } else {
                    transactionUnitModRpcKey = modChannelKeys.get(0);
                }

                MessageDto respMsg =
                        rpcClient.request(transactionUnitModRpcKey, MessageCreator.notifyUnit(notifyUnitParams));
                if (!MessageUtils.statusOk(respMsg)) {
                    // 提交/回滚失败的消息处理
                    List<Object> params = Arrays.asList(notifyUnitParams, transUnit.getModId());
                    rpcExceptionHandler.handleNotifyUnitBusinessException(params, respMsg.loadBean(Throwable.class));
                }
            } catch (RpcException e) {
                // 提交/回滚通讯失败
                List<Object> params = Arrays.asList(notifyUnitParams, transUnit.getModId());
                rpcExceptionHandler.handleNotifyUnitMessageException(params, e);
            } finally {
                txLogger.trace(dtxContext.groupId(), notifyUnitParams.getUnitId(), Transactions.TAG_TRANSACTION, "notify unit over");
            }
        }
    }

}
