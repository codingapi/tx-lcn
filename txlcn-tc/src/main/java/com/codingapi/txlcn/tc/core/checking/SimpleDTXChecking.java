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
package com.codingapi.txlcn.tc.core.checking;

import com.codingapi.txlcn.common.exception.TransactionClearException;
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import com.codingapi.txlcn.txmsg.params.TxExceptionParams;
import com.codingapi.txlcn.tc.config.TxClientConfig;
import com.codingapi.txlcn.tc.core.context.TCGlobalContext;
import com.codingapi.txlcn.tc.core.context.TxContext;
import com.codingapi.txlcn.tc.corelog.aspect.AspectLogger;
import com.codingapi.txlcn.tc.txmsg.ReliableMessenger;
import com.codingapi.txlcn.tc.txmsg.TMReporter;
import com.codingapi.txlcn.tc.core.template.TransactionCleanTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Description: 基于JDK任务调度线程池实现的DTX检测
 * Date: 2018/12/19
 *
 * @author ujued
 * @see DTXChecking
 */
@Component
@Slf4j
public class SimpleDTXChecking implements DTXChecking, DisposableBean {

    private static final Map<String, ScheduledFuture> delayTasks = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduledExecutorService =
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    private static final TxLogger txLogger = TxLogger.newLogger(SimpleDTXChecking.class);

    private TransactionCleanTemplate transactionCleanTemplate;

    private final ReliableMessenger reliableMessenger;

    private final TxClientConfig clientConfig;

    private final AspectLogger aspectLogger;

    private final TMReporter tmReporter;

    private final TCGlobalContext globalContext;

    @Autowired
    public SimpleDTXChecking(TxClientConfig clientConfig, AspectLogger aspectLogger, TMReporter tmReporter,
                             TCGlobalContext globalContext, ReliableMessenger reliableMessenger) {
        this.clientConfig = clientConfig;
        this.aspectLogger = aspectLogger;
        this.tmReporter = tmReporter;
        this.globalContext = globalContext;
        this.reliableMessenger = reliableMessenger;
    }

    public void setTransactionCleanTemplate(TransactionCleanTemplate transactionCleanTemplate) {
        this.transactionCleanTemplate = transactionCleanTemplate;
    }

    @Override
    public void startDelayCheckingAsync(String groupId, String unitId, String transactionType) {
        txLogger.taskTrace(groupId, unitId, "start delay checking task");
        ScheduledFuture scheduledFuture = scheduledExecutorService.schedule(() -> {
            try {
                TxContext txContext = globalContext.txContext(groupId);
                if (Objects.nonNull(txContext)) {
                    synchronized (txContext.getLock()) {
                        txLogger.taskTrace(groupId, unitId, "checking waiting for business code finish.");
                        txContext.getLock().wait();
                    }
                }
                int state = reliableMessenger.askTransactionState(groupId, unitId);
                txLogger.taskTrace(groupId, unitId, "ask transaction state {}", state);
                if (state == -1) {
                    txLogger.error(this.getClass().getSimpleName(), "delay clean transaction error.");
                    onAskTransactionStateException(groupId, unitId, transactionType);
                } else {
                    transactionCleanTemplate.clean(groupId, unitId, transactionType, state);
                    aspectLogger.clearLog(groupId, unitId);
                }

            } catch (RpcException e) {
                onAskTransactionStateException(groupId, unitId, transactionType);
            } catch (TransactionClearException | InterruptedException e) {
                txLogger.error(this.getClass().getSimpleName(), "{} clean transaction error.", transactionType);
            }
        }, clientConfig.getDtxTime(), TimeUnit.MILLISECONDS);
        delayTasks.put(groupId + unitId, scheduledFuture);
    }

    @Override
    public void stopDelayChecking(String groupId, String unitId) {
        ScheduledFuture scheduledFuture = delayTasks.get(groupId + unitId);
        if (Objects.nonNull(scheduledFuture)) {
            txLogger.taskTrace(groupId, unitId, "cancel {}:{} checking.", groupId, unitId);
            scheduledFuture.cancel(true);
        }
    }

    private void onAskTransactionStateException(String groupId, String unitId, String transactionType) {
        try {
            // 通知TxManager事务补偿
            tmReporter.reportTransactionState(groupId, unitId, TxExceptionParams.ASK_ERROR, 0);
            log.warn("{} > has compensation info!", transactionType);

            // 事务回滚, 保留适当的补偿信息
            transactionCleanTemplate.cleanWithoutAspectLog(groupId, unitId, transactionType, 0);
        } catch (TransactionClearException e) {
            txLogger.error(groupId, unitId, Transactions.TAG_TASK, "{} > clean transaction error.", transactionType);
        }
    }

    @Override
    public void destroy() {
        scheduledExecutorService.shutdown();
        try {
            // for non over tasks.
            scheduledExecutorService.awaitTermination(6, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
    }
}
