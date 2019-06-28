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

import com.codingapi.txlcn.common.exception.LcnBusinessException;
import com.codingapi.txlcn.common.exception.TransactionClearException;
import com.codingapi.txlcn.common.exception.TransactionException;
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
public class TransactionControlTemplate {

    private static final TxLogger txLogger = TxLogger.newLogger(TransactionControlTemplate.class);

    private final AspectLogger aspectLogger;

    private final DTXChecking dtxChecking;

    private final DTXExceptionHandler dtxExceptionHandler;

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final ReliableMessenger reliableMessenger;

    private final TCGlobalContext globalContext;

    @Autowired
    public TransactionControlTemplate(AspectLogger aspectLogger, DTXChecking dtxChecking,
                                      DTXExceptionHandler dtxExceptionHandler,
                                      TransactionCleanTemplate transactionCleanTemplate,
                                      ReliableMessenger reliableMessenger, TCGlobalContext globalContext) {
        this.aspectLogger = aspectLogger;
        this.dtxChecking = dtxChecking;
        this.dtxExceptionHandler = dtxExceptionHandler;
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.reliableMessenger = reliableMessenger;
        this.globalContext = globalContext;
    }

    /**
     * Client创建事务组操作集合
     *
     * @param groupId         groupId
     * @param unitId          unitId
     * @param transactionInfo txTrace
     * @param transactionType transactionType
     * @throws TransactionException 创建group失败时抛出
     */
    public void createGroup(String groupId, String unitId, TransactionInfo transactionInfo, String transactionType)
            throws TransactionException {
        //创建事务组
        try {
            // 日志
            txLogger.txTrace(groupId, unitId,
                    "create group > transaction type: {}", transactionType);
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
        txLogger.txTrace(groupId, unitId, "create group over");
    }

    /**
     * Client加入事务组操作集合
     *
     * @param groupId         groupId
     * @param unitId          unitId
     * @param transactionType transactionType
     * @param transactionInfo txTrace
     * @throws TransactionException 加入事务组失败时抛出
     */
    public void joinGroup(String groupId, String unitId, String transactionType, TransactionInfo transactionInfo)
            throws TransactionException {
        try {
            txLogger.txTrace(groupId, unitId, "join group > transaction type: {}", transactionType);

            reliableMessenger.joinGroup(groupId, unitId, transactionType, DTXLocalContext.transactionState(globalContext.dtxState(groupId)));

            txLogger.txTrace(groupId, unitId, "join group message over.");

            // 异步检测
            dtxChecking.startDelayCheckingAsync(groupId, unitId, transactionType);

            // 缓存参与方切面信息
            aspectLogger.trace(groupId, unitId, transactionInfo);
        } catch (RpcException e) {
            dtxExceptionHandler.handleJoinGroupMessageException(Arrays.asList(groupId, unitId, transactionType), e);
        } catch (LcnBusinessException e) {
            dtxExceptionHandler.handleJoinGroupBusinessException(Arrays.asList(groupId, unitId, transactionType), e);
        }
        txLogger.txTrace(groupId, unitId, "join group logic over");
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
            txLogger.txTrace(
                    groupId, unitId, "notify group > transaction type: {}, state: {}.", transactionType, state);
            if (globalContext.isDTXTimeout()) {
                txLogger.trace(groupId, unitId, Transactions.TE, "dtx timeout.");
                throw new LcnBusinessException("dtx timeout.");
            }
            state = reliableMessenger.notifyGroup(groupId, state);
            transactionCleanTemplate.clean(groupId, unitId, transactionType, state);
        } catch (TransactionClearException e) {
            txLogger.trace(groupId, unitId, Transactions.TE, "clean transaction fail.");
        } catch (RpcException e) {
            dtxExceptionHandler.handleNotifyGroupMessageException(Arrays.asList(groupId, state, unitId, transactionType), e);
        } catch (LcnBusinessException e) {
            // 关闭事务组失败
            dtxExceptionHandler.handleNotifyGroupBusinessException(Arrays.asList(groupId, state, unitId, transactionType), e);
        }
        txLogger.txTrace(groupId, unitId, "notify group exception state {}.", state);
    }
}
