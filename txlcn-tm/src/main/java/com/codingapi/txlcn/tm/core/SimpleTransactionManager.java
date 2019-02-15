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

import com.codingapi.txlcn.common.exception.TransactionException;
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tm.core.storage.TransactionUnit;
import com.codingapi.txlcn.tm.support.service.TxExceptionService;
import com.codingapi.txlcn.tm.txmsg.MessageCreator;
import com.codingapi.txlcn.tm.txmsg.RpcExceptionHandler;
import com.codingapi.txlcn.txmsg.RpcClient;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import com.codingapi.txlcn.txmsg.params.NotifyUnitParams;
import com.codingapi.txlcn.txmsg.util.MessageUtils;
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

    private static final TxLogger txLogger = TxLogger.newLogger(SimpleTransactionManager.class);

    private final RpcExceptionHandler rpcExceptionHandler;

    private final RpcClient rpcClient;

    private final TxExceptionService exceptionService;

    private final DTXContextRegistry dtxContextRegistry;

    @Autowired
    public SimpleTransactionManager(RpcExceptionHandler rpcExceptionHandler, RpcClient rpcClient,
                                    TxExceptionService exceptionService, DTXContextRegistry dtxContextRegistry) {
        this.rpcExceptionHandler = rpcExceptionHandler;
        this.exceptionService = exceptionService;
        this.rpcClient = rpcClient;
        this.dtxContextRegistry = dtxContextRegistry;
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

    @Override
    public int transactionStateFromFastStorage(String groupId) {
        return dtxContextRegistry.transactionState(groupId);
    }

    private void notifyTransaction(DTXContext dtxContext, int transactionState) throws TransactionException {
        List<TransactionUnit> transactionUnits = dtxContext.transactionUnits();
        log.debug("group[{}]'s transaction units: {}", dtxContext.getGroupId(), transactionUnits);
        for (TransactionUnit transUnit : transactionUnits) {
            NotifyUnitParams notifyUnitParams = new NotifyUnitParams();
            notifyUnitParams.setGroupId(dtxContext.getGroupId());
            notifyUnitParams.setUnitId(transUnit.getUnitId());
            notifyUnitParams.setUnitType(transUnit.getUnitType());
            notifyUnitParams.setState(transactionState);
            txLogger.txTrace(dtxContext.getGroupId(), notifyUnitParams.getUnitId(), "notify {}'s unit: {}",
                    transUnit.getModId(), transUnit.getUnitId());
            try {
                List<String> modChannelKeys = rpcClient.remoteKeys(transUnit.getModId());
                if (modChannelKeys.isEmpty()) {
                    // record exception
                    throw new RpcException("offline mod.");
                }
                MessageDto respMsg =
                        rpcClient.request(modChannelKeys.get(0), MessageCreator.notifyUnit(notifyUnitParams));
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
                txLogger.txTrace(dtxContext.getGroupId(), notifyUnitParams.getUnitId(), "notify unit over");
            }
        }
    }

}
