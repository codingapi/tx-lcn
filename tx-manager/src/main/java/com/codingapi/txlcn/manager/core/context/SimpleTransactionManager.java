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
package com.codingapi.txlcn.manager.core.context;

import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.params.NotifyUnitParams;
import com.codingapi.txlcn.spi.message.util.MessageUtils;
import com.codingapi.txlcn.commons.exception.JoinGroupException;
import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.commons.util.serializer.SerializerContext;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.commons.exception.TransactionException;
import com.codingapi.txlcn.manager.core.group.GroupRelationship;
import com.codingapi.txlcn.manager.core.group.TransUnit;
import com.codingapi.txlcn.manager.core.group.TransactionUnit;
import com.codingapi.txlcn.manager.core.message.MessageCreator;
import com.codingapi.txlcn.manager.core.message.RpcExceptionHandler;
import com.codingapi.txlcn.manager.support.service.TxExceptionService;
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

    private final GroupRelationship groupRelationship;

    private final RpcExceptionHandler rpcExceptionHandler;

    private final RpcClient rpcClient;

    private final TxLogger txLogger;

    private final TxExceptionService exceptionService;

    private final DTXTransactionContext transactionContext;

    @Autowired
    public SimpleTransactionManager(GroupRelationship groupRelationship,
                                    RpcExceptionHandler rpcExceptionHandler,
                                    RpcClient rpcClient, TxLogger txLogger,
                                    TxExceptionService exceptionService,
                                    DTXTransactionContext transactionContext) {
        this.rpcExceptionHandler = rpcExceptionHandler;
        this.groupRelationship = groupRelationship;
        this.exceptionService = exceptionService;
        this.rpcClient = rpcClient;
        this.txLogger = txLogger;
        this.transactionContext = transactionContext;
    }

    @Override
    public void begin(DTXTransaction dtxTransaction) {
        groupRelationship.createGroup(dtxTransaction.groupId());
    }

    @Override
    public void join(DTXTransaction dtxTransaction, TransactionUnit transactionUnit) throws TransactionException {
        TransUnit transUnit = new TransUnit();
        transUnit.setRemoteKey(transactionUnit.messageContextId());
        transUnit.setUnitType(transactionUnit.unitType());
        transUnit.setUnitId(transactionUnit.unitId());
        log.info("unit:{} joined group:{}", transactionUnit.unitId(), dtxTransaction.groupId());
        try {
            groupRelationship.joinGroup(dtxTransaction.groupId(), transUnit);
        } catch (JoinGroupException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public void commit(DTXTransaction transaction) {
        notifyTransaction(transaction.groupId(), 1);
    }

    @Override
    public void rollback(DTXTransaction transaction) {
        notifyTransaction(transaction.groupId(), 0);
    }

    @Override
    public void close(DTXTransaction groupTransaction) {
        transactionContext.destroyTransaction(groupTransaction.groupId());
        groupRelationship.removeGroup(groupTransaction.groupId());
    }

    @Override
    public int transactionState(DTXTransaction groupTransaction) {
        int state = exceptionService.transactionState(groupTransaction.groupId());
        if (state != -1) {
            return state;
        }
        return groupRelationship.transactionState(groupTransaction.groupId());
    }

    private void notifyTransaction(String groupId, int transactionState) {
        groupRelationship.setTransactionState(groupId, transactionState);
        List<TransUnit> transUnits = groupRelationship.unitsOfGroup(groupId);
        for (TransUnit transUnit : transUnits) {
            NotifyUnitParams notifyUnitParams = new NotifyUnitParams();
            notifyUnitParams.setGroupId(groupId);
            notifyUnitParams.setUnitId(transUnit.getUnitId());
            notifyUnitParams.setUnitType(transUnit.getUnitType());
            notifyUnitParams.setState(transactionState);
            txLogger.trace(groupId, notifyUnitParams.getUnitId(), Transactions.TAG_TRANSACTION, "notify unit");
            try {
                MessageDto respMsg =
                        rpcClient.request(transUnit.getRemoteKey(), MessageCreator.notifyUnit(notifyUnitParams));
                log.debug("notify unit: {}", transUnit.getRemoteKey());
                if (!MessageUtils.statusOk(respMsg)) {
                    // 提交/回滚失败的消息处理
                    List<Object> params = Arrays.asList(notifyUnitParams, transUnit.getRemoteKey());
                    rpcExceptionHandler.handleNotifyUnitBusinessException(params, throwable(respMsg.getBytes()));
                }
            } catch (RpcException | SerializerException e) {
                // 提交/回滚通讯失败
                List<Object> params = Arrays.asList(notifyUnitParams, transUnit.getRemoteKey());
                rpcExceptionHandler.handleNotifyUnitMessageException(params, e);
            } finally {
                txLogger.trace(groupId, notifyUnitParams.getUnitId(), Transactions.TAG_TRANSACTION, "notify unit over");
            }
        }
    }

    private Throwable throwable(byte[] data) throws SerializerException {
        return SerializerContext.getInstance().deSerialize(data, Throwable.class);
    }
}
