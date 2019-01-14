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

import com.codingapi.txlcn.client.aspectlog.AspectLogger;
import com.codingapi.txlcn.client.support.LCNTransactionBeanHelper;
import com.codingapi.txlcn.client.support.checking.DTXChecking;
import com.codingapi.txlcn.commons.exception.TransactionClearException;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: 事务清理模板
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component
@Slf4j
public class TransactionCleanTemplate {

    private final LCNTransactionBeanHelper transactionBeanHelper;

    private final DTXChecking dtxChecking;

    private final AspectLogger aspectLogger;

    private final TxLogger txLogger;

    @Autowired
    public TransactionCleanTemplate(LCNTransactionBeanHelper transactionBeanHelper,
                                    DTXChecking dtxChecking,
                                    AspectLogger aspectLogger,
                                    TxLogger txLogger) {
        this.transactionBeanHelper = transactionBeanHelper;
        this.dtxChecking = dtxChecking;
        this.aspectLogger = aspectLogger;
        this.txLogger = txLogger;
    }

    /**
     * 清理事务
     *
     * @param groupId groupId
     * @param unitId unitId
     * @param unitType unitType
     * @param state state
     * @throws TransactionClearException TransactionClearException
     */
    public void clean(String groupId, String unitId, String unitType, int state) throws TransactionClearException {
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "clean transaction");

        transactionBeanHelper.loadTransactionCleanService(unitType).clear(
                groupId, state, unitId, unitType
        );

        dtxChecking.stopDelayChecking(groupId, unitId);

        aspectLogger.clearLog(groupId, unitId);

        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "clean transaction over");

        log.info("clean transaction over");
    }

    /**
     * 清理事务（不清理切面日志）
     *
     * @param groupId groupId
     * @param unitId unitId
     * @param unitType unitType
     * @param state state
     * @throws TransactionClearException TransactionClearException
     */
    public void compensationClean(String groupId, String unitId, String unitType, int state) throws TransactionClearException {
        txLogger.trace(groupId, unitId, Transactions.TAG_TRANSACTION, "clean compensation transaction");
        transactionBeanHelper.loadTransactionCleanService(unitType).clear(
                groupId, state, unitId, unitType
        );

        dtxChecking.stopDelayChecking(groupId, unitId);
    }
}
