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
package com.codingapi.txlcn.tc.core.transaction.txc.control;

import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.TxcService;
import com.codingapi.txlcn.tc.core.TransactionCleanService;
import com.codingapi.txlcn.common.exception.TransactionClearException;
import com.codingapi.txlcn.common.exception.TxcLogicException;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.bean.StatementInfo;
import com.codingapi.txlcn.tc.txmsg.TMReporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component
@Slf4j
public class TxcTransactionCleanService implements TransactionCleanService {

    private final TxcService txcService;

    private final TMReporter tmReporter;

    @Autowired
    public TxcTransactionCleanService(TxcService txcService, TMReporter tmReporter) {
        this.txcService = txcService;
        this.tmReporter = tmReporter;
    }

    @Override
    public void clear(String groupId, int state, String unitId, String unitType) throws TransactionClearException {
        boolean rethrowTxcException = false;
        try {
            // 若需要回滚读undo_log，进行回滚
            if (state == 0) {
                txcService.undo(groupId, unitId);
            }
        } catch (TxcLogicException e) {
            @SuppressWarnings("unchecked")
            List<StatementInfo> statementInfoList = (List<StatementInfo>) e.getAttachment();
            tmReporter.reportTxcUndoException(groupId, unitId, statementInfoList);
            rethrowTxcException = true;
            log.debug("need compensation !");
        }

        try {
            // 清理TXC
            txcService.cleanTxc(groupId, unitId);
        } catch (TxcLogicException e) {
            throw new TransactionClearException(e);
        }

        if (rethrowTxcException) {
            throw TransactionClearException.needCompensation();
        }
    }
}
