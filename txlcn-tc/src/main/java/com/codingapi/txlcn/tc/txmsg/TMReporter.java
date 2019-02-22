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
package com.codingapi.txlcn.tc.txmsg;

import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.bean.StatementInfo;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import com.codingapi.txlcn.txmsg.params.TxExceptionParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description: 客户端上报Manager
 * Date: 2018/12/29
 *
 * @author ujued
 */
@Component
public class TMReporter {

    private final ReliableMessenger reliableMessenger;

    private static final TxLogger txLogger = TxLogger.newLogger(TMReporter.class);

    private static final String REPORT_ERROR_MESSAGE = "report transaction transactionState error";

    @Autowired
    public TMReporter(ReliableMessenger reliableMessenger) {
        this.reliableMessenger = reliableMessenger;
    }

    /**
     * Manager 记录事务状态
     *
     * @param groupId   groupId
     * @param unitId    unitId
     * @param registrar registrar
     * @param state     transactionState
     */
    public void reportTransactionState(String groupId, String unitId, Short registrar, int state) {
        TxExceptionParams txExceptionParams = new TxExceptionParams();
        txExceptionParams.setGroupId(groupId);
        txExceptionParams.setRegistrar(registrar);
        txExceptionParams.setTransactionState(state);
        txExceptionParams.setUnitId(unitId);
        report(txExceptionParams);
    }

    /**
     * Manager 记录TCC清理事务失败
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @param state   state
     */
    public void reportTccCleanException(String groupId, String unitId, int state) {
        TxExceptionParams txExceptionParams = new TxExceptionParams();
        txExceptionParams.setGroupId(groupId);
        txExceptionParams.setRegistrar(TxExceptionParams.TCC_CLEAN_ERROR);
        txExceptionParams.setTransactionState(state);
        txExceptionParams.setUnitId(unitId);
        report(txExceptionParams);
    }

    private void report(TxExceptionParams exceptionParams) {
        try {
            reliableMessenger.request(MessageCreator.writeTxException(exceptionParams));
        } catch (RpcException e) {
            txLogger.trace(exceptionParams.getGroupId(), exceptionParams.getUnitId(), "TM report", REPORT_ERROR_MESSAGE);
        }
    }

    public void reportTxcUndoException(String groupId, String unitId, List<StatementInfo> statementInfoList) {
        TxExceptionParams exceptionParams = new TxExceptionParams();
        exceptionParams.setGroupId(groupId);
        exceptionParams.setUnitId(unitId);
        exceptionParams.setRegistrar(TxExceptionParams.TXC_UNDO_ERROR);
        exceptionParams.setTransactionState(0);
        exceptionParams.setRemark(statementInfoList.toString());
        report(exceptionParams);
    }
}
