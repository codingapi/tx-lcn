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
package com.codingapi.txlcn.tc.message.transaction;

import com.codingapi.txlcn.commons.exception.TransactionClearException;
import com.codingapi.txlcn.commons.exception.TxClientException;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.spi.message.params.NotifyUnitParams;
import com.codingapi.txlcn.tc.message.helper.RpcExecuteService;
import com.codingapi.txlcn.tc.message.helper.TransactionCmd;
import com.codingapi.txlcn.tc.support.context.DTXContext;
import com.codingapi.txlcn.tc.support.context.TCGlobalContext;
import com.codingapi.txlcn.tc.support.template.TransactionCleanTemplate;

import java.io.Serializable;
import java.util.Objects;

/**
 * Description: 默认RPC命令业务
 * Date: 2018/12/20
 *
 * @author ujued
 */
public class DefaultNotifiedUnitService implements RpcExecuteService {

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final TxLogger txLogger;

    private TCGlobalContext context;

    public DefaultNotifiedUnitService(TransactionCleanTemplate transactionCleanTemplate,
                                      TxLogger txLogger, TCGlobalContext context) {
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.txLogger = txLogger;
        this.context = context;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxClientException {
        try {
            NotifyUnitParams notifyUnitParams = transactionCmd.getMsg().loadBean(NotifyUnitParams.class);
            // 保证业务线程执行完毕后执行事务清理操作
            DTXContext dtxContext = context.dtxContext(transactionCmd.getGroupId());
            if (Objects.nonNull(dtxContext)) {
                synchronized (dtxContext.getLock()) {
                    txLogger.trace(transactionCmd.getGroupId(), notifyUnitParams.getUnitId(), Transactions.TAG_TRANSACTION,
                            "clean transaction cmd waiting for business code finish.");
                    dtxContext.getLock().wait();
                }
            }
            // 事务清理操作
            transactionCleanTemplate.clean(
                    notifyUnitParams.getGroupId(),
                    notifyUnitParams.getUnitId(),
                    notifyUnitParams.getUnitType(),
                    notifyUnitParams.getState());
            return null;
        } catch (TransactionClearException | InterruptedException e) {
            throw new TxClientException(e);
        }
    }
}
