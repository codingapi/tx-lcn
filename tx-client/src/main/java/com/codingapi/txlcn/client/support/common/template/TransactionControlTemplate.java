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
package com.codingapi.txlcn.client.support.common.template;

import com.codingapi.txlcn.client.support.checking.DTXChecking;
import com.codingapi.txlcn.client.support.checking.DTXExceptionHandler;
import com.codingapi.txlcn.client.aspectlog.AspectLogger;
import com.codingapi.txlcn.client.message.helper.MessageCreator;
import com.codingapi.txlcn.commons.bean.TransactionInfo;
import com.codingapi.txlcn.commons.exception.BeforeBusinessException;
import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.exception.TransactionClearException;
import com.codingapi.txlcn.commons.exception.TxClientException;
import com.codingapi.txlcn.commons.util.serializer.SerializerContext;
import com.codingapi.txlcn.spi.message.params.JoinGroupParams;
import com.codingapi.txlcn.spi.message.params.NotifyGroupParams;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.util.MessageUtils;
import com.codingapi.txlcn.spi.sleuth.TracerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

/**
 * Description:
 * Date: 2018/12/20
 *
 * @author ujued
 */
@Component
@Slf4j
public class TransactionControlTemplate {

    private final RpcClient rpcClient;

    private final TracerHelper tracerHelper;

    private final AspectLogger aspectLogger;

    private final DTXChecking dtxChecking;


    private final DTXExceptionHandler dtxExceptionHandler;

    private final TransactionCleanTemplate transactionCleanTemplate;

    @Autowired
    private TxLogger txLogger;

    @Autowired
    public TransactionControlTemplate(RpcClient rpcClient,
                                      TracerHelper tracerHelper,
                                      AspectLogger aspectLogger,
                                      DTXChecking dtxChecking,
                                      DTXExceptionHandler dtxExceptionHandler,
                                      TransactionCleanTemplate transactionCleanTemplate) {
        this.rpcClient = rpcClient;
        this.tracerHelper = tracerHelper;
        this.aspectLogger = aspectLogger;
        this.dtxChecking = dtxChecking;
        this.dtxExceptionHandler = dtxExceptionHandler;
        this.transactionCleanTemplate = transactionCleanTemplate;
    }

    /**
     * Client创建事务组操作集合
     *
     * @param groupId groupId
     * @param groupId unitId
     * @param transactionInfo transactionInfo
     * @param transactionType transactionType
     * @throws BeforeBusinessException 创建group失败时抛出
     */
    public void createGroup(String groupId, String unitId, TransactionInfo transactionInfo, String transactionType)
            throws BeforeBusinessException {
        //创建事务组
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "create group");
        String remoteKey = null;
        try {
            // 负载一个TxManager
            remoteKey = rpcClient.loadRemoteKey();
            // groupId传递
            tracerHelper.createGroupId(groupId, remoteKey);

            // 日志
            log.debug("transaction type[{}] > create group > groupId: {}, unitId: {}, remoteKey: {}",
                    transactionType, groupId, unitId, remoteKey);

            // TxManager创建事务组
            MessageDto messageDto = rpcClient.request(remoteKey, MessageCreator.createGroup(groupId));
            if (MessageUtils.statusOk(messageDto)) {
                log.info("{} > create transaction group: {}, txManager: {}.", transactionType, groupId, remoteKey);
                // 缓存发起方切面信息
                aspectLogger.trace(groupId, unitId, transactionInfo);
                return;
            }
            // 创建事务组业务失败
            dtxExceptionHandler.handleCreateGroupBusinessException(
                    Arrays.asList(groupId, remoteKey), new Exception("TxManager业务异常"));
        } catch (RpcException e) {

            // 通讯异常
            dtxExceptionHandler.handleCreateGroupMessageException(Arrays.asList(groupId, remoteKey), e);
        }
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "create group over");
    }

    /**
     * Client加入事务组操作集合
     *
     * @param groupId groupId
     * @param unitId unitId
     * @param transactionType transactionType
     * @param transactionInfo transactionInfo
     * @throws TxClientException 加入事务组失败时抛出
     */
    public void joinGroup(String groupId, String unitId, String transactionType, TransactionInfo transactionInfo)
            throws TxClientException {
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "join group");
        String managerKey = Optional.ofNullable(tracerHelper.getTxManagerKey()).orElseThrow(() -> new RuntimeException("sleuth pass error."));
        JoinGroupParams joinGroupParams = new JoinGroupParams();
        joinGroupParams.setGroupId(groupId);
        joinGroupParams.setUnitId(unitId);
        joinGroupParams.setUnitType(transactionType);
        // 日志
        log.debug("transaction type[{}] > join group > groupId: {}, unitId: {}, remoteKey: {}",
                transactionType, groupId, unitId, managerKey);
        try {
            MessageDto messageDto = rpcClient.request(managerKey, MessageCreator.joinGroup(joinGroupParams));
            if (MessageUtils.statusOk(messageDto)) {
                log.info("{} > joined group.", transactionType);

                // 异步检测
                dtxChecking.startDelayCheckingAsync(groupId, unitId, transactionType);

                // 缓存参与方切面信息
                aspectLogger.trace(groupId, unitId, transactionInfo);
                return;
            }
            dtxExceptionHandler.handleJoinGroupBusinessException(joinGroupParams, new Exception("TxManager加入事务组业务异常"));
        } catch (RpcException e) {
            dtxExceptionHandler.handleJoinGroupMessageException(joinGroupParams, e);
        }

        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "join group over");
    }

    /**
     * Client通知事务组操作集合
     *
     * @param groupId groupId
     * @param unitId unitId
     * @param transactionType transactionType
     * @param state state
     */
    public void notifyGroup(String groupId, String unitId, String transactionType, int state) {
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "notify group " + state);
        NotifyGroupParams notifyGroupParams = new NotifyGroupParams();
        notifyGroupParams.setGroupId(groupId);
        notifyGroupParams.setState(state);
        String managerKey = Optional.ofNullable(tracerHelper.getTxManagerKey()).orElseThrow(() -> new RuntimeException("sleuth pass error."));
        // 日志
        log.debug("transaction type[{}] > close group > groupId: {}, unitId: {}, remoteKey: {}",
                transactionType, groupId, unitId, managerKey);
        try {
            MessageDto messageDto =
                    rpcClient.request(managerKey, MessageCreator.notifyGroup(notifyGroupParams));
            // 成功清理发起方事务
            if (MessageUtils.statusOk(messageDto)) {
                log.info("{} > close transaction group.", transactionType);
                transactionCleanTemplate.clean(groupId, unitId, transactionType, state);
                return;
            }
            // 关闭事务组失败
            dtxExceptionHandler.handleNotifyGroupBusinessException(
                    Arrays.asList(notifyGroupParams, unitId, transactionType),
                    SerializerContext.getInstance().deSerialize(messageDto.getBytes(), Throwable.class)
            );
        } catch (TransactionClearException e) {
            log.error("clear exception", e);
        } catch (RpcException e) {
            dtxExceptionHandler.handleNotifyGroupMessageException(
                    Arrays.asList(notifyGroupParams, unitId, transactionType), e
            );
        } catch (SerializerException e) {
            dtxExceptionHandler.handleNotifyGroupBusinessException(
                    Arrays.asList(notifyGroupParams, unitId, transactionType), e
            );
        }

        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "notify group exception " + state);
    }
}
