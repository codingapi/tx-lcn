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
package com.codingapi.txlcn.tc.txmsg.transaction;

import com.codingapi.txlcn.common.exception.TransactionClearException;
import com.codingapi.txlcn.common.exception.TxClientException;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.txmsg.params.NotifyUnitParams;
import com.codingapi.txlcn.tc.txmsg.RpcExecuteService;
import com.codingapi.txlcn.tc.txmsg.TransactionCmd;
import com.codingapi.txlcn.tc.core.context.TxContext;
import com.codingapi.txlcn.tc.core.context.TCGlobalContext;
import com.codingapi.txlcn.tc.core.template.TransactionCleanTemplate;

import java.io.Serializable;
import java.util.Objects;

/**
 * Description: 默认RPC命令业务
 * Date: 2018/12/20
 *
 * @author ujued
 */
public class DefaultNotifiedUnitService implements RpcExecuteService {

    private static final TxLogger txLogger = TxLogger.newLogger(DefaultNotifiedUnitService.class);

    private final TransactionCleanTemplate transactionCleanTemplate;

    private TCGlobalContext globalContext;

    public DefaultNotifiedUnitService(TransactionCleanTemplate transactionCleanTemplate, TCGlobalContext globalContext) {
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.globalContext = globalContext;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxClientException {
        try {
            NotifyUnitParams notifyUnitParams = transactionCmd.getMsg().loadBean(NotifyUnitParams.class);
            // 保证业务线程执行完毕后执行事务清理操作
            TxContext txContext = globalContext.txContext(transactionCmd.getGroupId());
            if (Objects.nonNull(txContext)) {
                synchronized (txContext.getLock()) {
                    txLogger.txTrace(transactionCmd.getGroupId(), notifyUnitParams.getUnitId(),
                            "clean transaction cmd waiting for business code finish.");
                    txContext.getLock().wait();
                }
            }
            // 事务清理操作
            transactionCleanTemplate.clean(
                    notifyUnitParams.getGroupId(),
                    notifyUnitParams.getUnitId(),
                    notifyUnitParams.getUnitType(),
                    notifyUnitParams.getState());
            return true;
        } catch (TransactionClearException | InterruptedException e) {
            throw new TxClientException(e);
        }
    }
}
