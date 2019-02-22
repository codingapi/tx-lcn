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

import com.codingapi.txlcn.common.exception.TransactionClearException;
import com.codingapi.txlcn.common.exception.TransactionException;
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.DTXLocalControl;
import com.codingapi.txlcn.tc.core.TxTransactionInfo;
import com.codingapi.txlcn.tc.core.template.TransactionCleanTemplate;
import com.codingapi.txlcn.tc.core.template.TransactionControlTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component("control_txc_running")
@Slf4j
public class TxcRunningTransaction implements DTXLocalControl {

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final TransactionControlTemplate transactionControlTemplate;

    private static final TxLogger txLogger = TxLogger.newLogger(TxcRunningTransaction.class);

    @Autowired
    public TxcRunningTransaction(TransactionCleanTemplate transactionCleanTemplate,
                                 TransactionControlTemplate transactionControlTemplate) {
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.transactionControlTemplate = transactionControlTemplate;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) {
        // TXC 类型事务需要代理资源
        DTXLocalContext.makeProxy();
    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        try {
            log.debug("txc > running > clean transaction.");
            transactionCleanTemplate.clean(info.getGroupId(), info.getUnitId(), info.getTransactionType(), 0);
        } catch (TransactionClearException e) {
            log.error("txc > Clean Transaction Error", e);
            txLogger.trace(info.getGroupId(), info.getUnitId(), Transactions.TE, "clean transaction error");
        }
    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) throws TransactionException {
        // 加入事务组
        transactionControlTemplate.joinGroup(info.getGroupId(), info.getUnitId(), info.getTransactionType(),
                info.getTransactionInfo());
    }
}
