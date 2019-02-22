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

import com.codingapi.txlcn.common.exception.TransactionClearException;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tc.core.checking.DTXChecking;
import com.codingapi.txlcn.tc.core.context.TCGlobalContext;
import com.codingapi.txlcn.tc.corelog.aspect.AspectLogger;
import com.codingapi.txlcn.tc.support.TxLcnBeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: 事务清理模板
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component
public class TransactionCleanTemplate {

    private final TxLcnBeanHelper transactionBeanHelper;

    private final DTXChecking dtxChecking;

    private final AspectLogger aspectLogger;

    private static final TxLogger txLogger = TxLogger.newLogger(TransactionCleanTemplate.class);

    private final TCGlobalContext globalContext;

    @Autowired
    public TransactionCleanTemplate(TxLcnBeanHelper transactionBeanHelper, DTXChecking dtxChecking,
                                    AspectLogger aspectLogger, TCGlobalContext globalContext) {
        this.transactionBeanHelper = transactionBeanHelper;
        this.dtxChecking = dtxChecking;
        this.aspectLogger = aspectLogger;
        this.globalContext = globalContext;
    }

    /**
     * 清理事务
     *
     * @param groupId  groupId
     * @param unitId   unitId
     * @param unitType unitType
     * @param state    transactionState
     * @throws TransactionClearException TransactionClearException
     */
    public void clean(String groupId, String unitId, String unitType, int state) throws TransactionClearException {
        txLogger.txTrace(groupId, unitId, "clean transaction");
        try {
            cleanWithoutAspectLog(groupId, unitId, unitType, state);
            aspectLogger.clearLog(groupId, unitId);
        } catch (TransactionClearException e) {
            if (!e.isNeedCompensation()) {
                aspectLogger.clearLog(groupId, unitId);
            }
        } catch (Throwable throwable) {
            aspectLogger.clearLog(groupId, unitId);
        }
        txLogger.txTrace(groupId, unitId, "clean transaction over");
    }

    /**
     * 清理事务（不清理切面日志）
     *
     * @param groupId  groupId
     * @param unitId   unitId
     * @param unitType unitType
     * @param state    transactionState
     * @throws TransactionClearException TransactionClearException
     */
    public void cleanWithoutAspectLog(String groupId, String unitId, String unitType, int state) throws TransactionClearException {
        try {
            transactionBeanHelper.loadTransactionCleanService(unitType).clear(
                    groupId, state, unitId, unitType
            );
        } finally {
            globalContext.clearGroup(groupId);

            dtxChecking.stopDelayChecking(groupId, unitId);
        }
    }
}
