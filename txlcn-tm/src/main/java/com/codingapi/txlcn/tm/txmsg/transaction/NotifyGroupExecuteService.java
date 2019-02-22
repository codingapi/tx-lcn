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
package com.codingapi.txlcn.tm.txmsg.transaction;

import com.codingapi.txlcn.common.exception.TransactionException;
import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tm.core.DTXContext;
import com.codingapi.txlcn.tm.core.DTXContextRegistry;
import com.codingapi.txlcn.tm.core.TransactionManager;
import com.codingapi.txlcn.tm.txmsg.RpcExecuteService;
import com.codingapi.txlcn.tm.txmsg.TransactionCmd;
import com.codingapi.txlcn.txmsg.params.NotifyGroupParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author ujued
 */
@Service("rpc_notify-group")
public class NotifyGroupExecuteService implements RpcExecuteService {

    private static final TxLogger txLogger = TxLogger.newLogger(NotifyGroupExecuteService.class);

    private final TransactionManager transactionManager;

    private final DTXContextRegistry dtxContextRegistry;

    @Autowired
    public NotifyGroupExecuteService(TransactionManager transactionManager, DTXContextRegistry dtxContextRegistry) {
        this.transactionManager = transactionManager;
        this.dtxContextRegistry = dtxContextRegistry;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            DTXContext dtxContext = dtxContextRegistry.get(transactionCmd.getGroupId());
            // 解析参数
            NotifyGroupParams notifyGroupParams = transactionCmd.getMsg().loadBean(NotifyGroupParams.class);
            int commitState = notifyGroupParams.getState();
            // 获取事务状态（当手动回滚时会先设置状态）
            int transactionState = transactionManager.transactionStateFromFastStorage(transactionCmd.getGroupId());
            if (transactionState == 0) {
                commitState = 0;
            }

            // 系统日志
            txLogger.txTrace(
                    transactionCmd.getGroupId(), "", "notify group state: {}", notifyGroupParams.getState());

            if (commitState == 1) {
                transactionManager.commit(dtxContext);
            } else if (commitState == 0) {
                transactionManager.rollback(dtxContext);
            }
            if (transactionState == 0) {
                txLogger.txTrace(transactionCmd.getGroupId(), "", "mandatory rollback for user.");
            }
            return commitState;
        } catch (TransactionException e) {
            throw new TxManagerException(e);
        } finally {
            transactionManager.close(transactionCmd.getGroupId());
            // 系统日志
            txLogger.txTrace(transactionCmd.getGroupId(), "", "notify group successfully.");
        }
    }
}
