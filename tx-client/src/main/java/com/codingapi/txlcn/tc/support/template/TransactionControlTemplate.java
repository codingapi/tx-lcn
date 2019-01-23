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
package com.codingapi.txlcn.tc.support.template;

import com.codingapi.txlcn.commons.bean.TransactionInfo;
import com.codingapi.txlcn.commons.exception.BeforeBusinessException;
import com.codingapi.txlcn.commons.exception.LcnBusinessException;
import com.codingapi.txlcn.commons.exception.TransactionClearException;
import com.codingapi.txlcn.commons.exception.TxClientException;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.sleuth.TracerHelper;
import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.corelog.aspect.AspectLogger;
import com.codingapi.txlcn.tc.message.ReliableMessenger;
import com.codingapi.txlcn.tc.message.TMSearcher;
import com.codingapi.txlcn.tc.support.checking.DTXChecking;
import com.codingapi.txlcn.tc.support.checking.DTXExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Description:
 * Date: 2018/12/20
 *
 * @author ujued
 */
@Component
@Slf4j
public class TransactionControlTemplate {

    private final TracerHelper tracerHelper;

    private final AspectLogger aspectLogger;

    private final DTXChecking dtxChecking;

    private final DTXExceptionHandler dtxExceptionHandler;

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final TxLogger txLogger;

    private final ReliableMessenger reliableMessenger;

    @Autowired
    public TransactionControlTemplate(TracerHelper tracerHelper, AspectLogger aspectLogger,
                                      DTXChecking dtxChecking, DTXExceptionHandler dtxExceptionHandler,
                                      TransactionCleanTemplate transactionCleanTemplate,
                                      TxLogger txLogger, ReliableMessenger reliableMessenger) {
        this.tracerHelper = tracerHelper;
        this.aspectLogger = aspectLogger;
        this.dtxChecking = dtxChecking;
        this.dtxExceptionHandler = dtxExceptionHandler;
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.txLogger = txLogger;
        this.reliableMessenger = reliableMessenger;
    }

    /**
     * Client创建事务组操作集合
     *
     * @param groupId         groupId
     * @param unitId          unitId
     * @param transactionInfo transactionInfo
     * @param transactionType transactionType
     * @throws BeforeBusinessException 创建group失败时抛出
     */
    public void createGroup(String groupId, String unitId, TransactionInfo transactionInfo, String transactionType)
            throws BeforeBusinessException {
        //创建事务组
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "create group");
        try {
            // groupId传递
            tracerHelper.createGroupId(groupId);

            // 日志
            log.debug("transaction type[{}] > create group > groupId: {}, unitId: {}", transactionType, groupId, unitId);
            reliableMessenger.createGroup(groupId);

            // 缓存发起方切面信息
            aspectLogger.trace(groupId, unitId, transactionInfo);
        } catch (RpcException e) {
            if (e.getCode() == RpcException.NON_TX_MANAGER) {
                TMSearcher.search();
            }
            // 通讯异常
            dtxExceptionHandler.handleCreateGroupMessageException(groupId, e);
        } catch (LcnBusinessException e) {
            // 创建事务组业务失败
            dtxExceptionHandler.handleCreateGroupBusinessException(groupId, e.getCause());
        }
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "create group over");
    }

    /**
     * Client加入事务组操作集合
     *
     * @param groupId         groupId
     * @param unitId          unitId
     * @param transactionType transactionType
     * @param transactionInfo transactionInfo
     * @throws TxClientException 加入事务组失败时抛出
     */
    public void joinGroup(String groupId, String unitId, String transactionType, TransactionInfo transactionInfo)
            throws TxClientException {
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "join group");
        // 日志
        log.debug("transaction type[{}] > join group > groupId: {}, unitId: {}", transactionType, groupId, unitId);
        try {
            reliableMessenger.joinGroup(groupId, unitId, transactionType, DTXLocalContext.transactionState());

            log.debug("{} > joined group.", transactionType);

            // 异步检测
            dtxChecking.startDelayCheckingAsync(groupId, unitId, transactionType);

            // 缓存参与方切面信息
            aspectLogger.trace(groupId, unitId, transactionInfo);
        } catch (RpcException e) {
            if (e.getCode() == RpcException.NON_TX_MANAGER) {
                TMSearcher.search();
            }
            dtxExceptionHandler.handleJoinGroupMessageException(Arrays.asList(groupId, unitId, transactionType), e);
        } catch (LcnBusinessException e) {
            dtxExceptionHandler.handleJoinGroupBusinessException(Arrays.asList(groupId, unitId, transactionType), e.getCause());
        }
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "join group over");
    }

    /**
     * Client通知事务组操作集合
     *
     * @param groupId         groupId
     * @param unitId          unitId
     * @param transactionType transactionType
     * @param state           transactionState
     */
    public void notifyGroup(String groupId, String unitId, String transactionType, int state) {
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "notify group " + state);
        log.debug("transaction type[{}] > notify group > groupId: {}, unitId: {}", transactionType, groupId, unitId);
        try {
            reliableMessenger.notifyGroup(groupId, state);
            transactionCleanTemplate.clean(groupId, unitId, transactionType, state);
            log.debug("{} > close transaction group.", transactionType);
        } catch (TransactionClearException e) {
            log.error("clear exception", e);
            txLogger.trace(groupId, unitId, Transactions.TE, "clean transaction fail.");
        } catch (RpcException e) {
            if (e.getCode() == RpcException.NON_TX_MANAGER) {
                TMSearcher.search();
            }
            dtxExceptionHandler.handleNotifyGroupMessageException(Arrays.asList(groupId, state, unitId, transactionType), e);
        } catch (LcnBusinessException e) {
            // 关闭事务组失败
            dtxExceptionHandler.handleNotifyGroupBusinessException(Arrays.asList(groupId, state, unitId, transactionType), e.getCause());
        }
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "notify group exception " + state);
    }
}