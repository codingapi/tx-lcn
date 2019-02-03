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

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.common.exception.TransactionException;
import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.common.exception.UserRollbackException;
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tm.core.DTXContext;
import com.codingapi.txlcn.tm.core.DTXContextRegistry;
import com.codingapi.txlcn.tm.core.TransactionManager;
import com.codingapi.txlcn.tm.txmsg.RpcExecuteService;
import com.codingapi.txlcn.tm.txmsg.TransactionCmd;
import com.codingapi.txlcn.txmsg.params.NotifyGroupParams;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class NotifyGroupExecuteService implements RpcExecuteService {

    private final TxLogger txLogger;

    private final TransactionManager transactionManager;

    private final DTXContextRegistry dtxContextRegistry;

    @Autowired
    public NotifyGroupExecuteService(TxLogger txLogger, TransactionManager transactionManager,
                                     DTXContextRegistry dtxContextRegistry) {
        this.txLogger = txLogger;
        this.transactionManager = transactionManager;
        this.dtxContextRegistry = dtxContextRegistry;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            DTXContext dtxContext = dtxContextRegistry.get(transactionCmd.getGroupId());
            // 解析参数
            NotifyGroupParams notifyGroupParams = transactionCmd.getMsg().loadBean(NotifyGroupParams.class);
            log.debug("notify group params: {}", JSON.toJSONString(notifyGroupParams));

            int commitState = notifyGroupParams.getState();
            // 获取事务状态（当手动回滚时会先设置状态）
            int transactionState = transactionManager.transactionStateFromFastStorage(transactionCmd.getGroupId());
            boolean hasThrow = false;
            if (transactionState == 0) {
                commitState = 0;
                hasThrow = true;
            }

            // 系统日志
            txLogger.transactionInfo(
                    transactionCmd.getGroupId(), "", "notify group state: %d", notifyGroupParams.getState());

            if (commitState == 1) {
                transactionManager.commit(dtxContext);
            } else if (commitState == 0) {
                transactionManager.rollback(dtxContext);
            }
            if (hasThrow) {
                throw new UserRollbackException("user mandatory rollback");
            }
        } catch (TransactionException e) {
            throw new TxManagerException(e);
        } finally {
            transactionManager.close(transactionCmd.getGroupId());
            // 系统日志
            txLogger.transactionInfo(transactionCmd.getGroupId(), "", "notify group over");
        }
        return null;
    }
}
