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
package com.codingapi.txlcn.tc.core.template;

import com.codingapi.txlcn.common.exception.*;
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tc.aspect.TransactionInfo;
import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.checking.DTXChecking;
import com.codingapi.txlcn.tc.core.checking.DTXExceptionHandler;
import com.codingapi.txlcn.tc.core.context.TCGlobalContext;
import com.codingapi.txlcn.tc.corelog.aspect.AspectLogger;
import com.codingapi.txlcn.tc.txmsg.ReliableMessenger;
import com.codingapi.txlcn.txmsg.exception.RpcException;
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

    private final AspectLogger aspectLogger;

    private final DTXChecking dtxChecking;

    private final DTXExceptionHandler dtxExceptionHandler;

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final TxLogger txLogger;

    private final ReliableMessenger reliableMessenger;

    private final TCGlobalContext globalContext;

    @Autowired
    public TransactionControlTemplate(AspectLogger aspectLogger, DTXChecking dtxChecking, TxLogger txLogger,
                                      DTXExceptionHandler dtxExceptionHandler,
                                      TransactionCleanTemplate transactionCleanTemplate,
                                      ReliableMessenger reliableMessenger, TCGlobalContext globalContext) {
        this.aspectLogger = aspectLogger;
        this.dtxChecking = dtxChecking;
        this.dtxExceptionHandler = dtxExceptionHandler;
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.txLogger = txLogger;
        this.reliableMessenger = reliableMessenger;
        this.globalContext = globalContext;
    }

    /**
     * Client创建事务组操作集合
     *
     * @param groupId         groupId
     * @param unitId          unitId
     * @param transactionInfo transactionInfo
     * @param transactionType transactionType
     * @throws TransactionException 创建group失败时抛出
     */
    public void createGroup(String groupId, String unitId, TransactionInfo transactionInfo, String transactionType)
            throws TransactionException {
        //创建事务组
        try {
            // 日志
            txLogger.transactionInfo(groupId, unitId,
                    "create group > {} > groupId: {xid}, unitId: {uid}", transactionType);
            // 创建事务组消息
            reliableMessenger.createGroup(groupId);
            // 缓存发起方切面信息
            aspectLogger.trace(groupId, unitId, transactionInfo);
        } catch (RpcException e) {
            // 通讯异常
            dtxExceptionHandler.handleCreateGroupMessageException(groupId, e);
        } catch (LcnBusinessException e) {
            // 创建事务组业务失败
            dtxExceptionHandler.handleCreateGroupBusinessException(groupId, e.getCause());
        }
        txLogger.transactionInfo(groupId, unitId, "create group over");
    }

    /**
     * Client加入事务组操作集合
     *
     * @param groupId         groupId
     * @param unitId          unitId
     * @param transactionType transactionType
     * @param transactionInfo transactionInfo
     * @throws TransactionException 加入事务组失败时抛出
     */
    public void joinGroup(String groupId, String unitId, String transactionType, TransactionInfo transactionInfo)
            throws TransactionException {
        try {
            txLogger.transactionInfo(groupId, unitId, "join group > {} > groupId: {xid}, unitId: {uid}", transactionType);

            reliableMessenger.joinGroup(groupId, unitId, transactionType, DTXLocalContext.transactionState());

            txLogger.transactionInfo(groupId, unitId, "{xid} join group message over.");

            // 异步检测
            dtxChecking.startDelayCheckingAsync(groupId, unitId, transactionType);

            // 缓存参与方切面信息
            aspectLogger.trace(groupId, unitId, transactionInfo);
        } catch (RpcException e) {
            dtxExceptionHandler.handleJoinGroupMessageException(Arrays.asList(groupId, unitId, transactionType), e);
        } catch (LcnBusinessException e) {
            dtxExceptionHandler.handleJoinGroupBusinessException(Arrays.asList(groupId, unitId, transactionType), e);
        }
        txLogger.transactionInfo(groupId, unitId, "join logic group over");
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
        try {
            txLogger.transactionInfo(
                    groupId, unitId, "notify group > {} > groupId: {xid}, unitId: {uid}, state: {}.", transactionType, state);
            if (globalContext.isDTXTimeout()) {
                throw new LcnBusinessException("dtx timeout.");
            }
            reliableMessenger.notifyGroup(groupId, state);
            transactionCleanTemplate.clean(groupId, unitId, transactionType, state);
            log.debug("{} > close transaction group.", transactionType);
        } catch (TransactionClearException e) {
            log.error("clear exception", e);
            txLogger.trace(groupId, unitId, Transactions.TE, "clean transaction fail.");
        } catch (RpcException e) {
            dtxExceptionHandler.handleNotifyGroupMessageException(Arrays.asList(groupId, state, unitId, transactionType), e);
        } catch (LcnBusinessException e) {
            // 关闭事务组失败
            dtxExceptionHandler.handleNotifyGroupBusinessException(Arrays.asList(groupId, state, unitId, transactionType), e.getCause());
        }
        txLogger.transactionInfo(groupId, unitId, "notify group exception state %d.", state);
    }
}
