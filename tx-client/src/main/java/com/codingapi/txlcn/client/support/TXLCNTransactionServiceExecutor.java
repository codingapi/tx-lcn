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
package com.codingapi.txlcn.client.support;


import com.codingapi.txlcn.client.bean.TxTransactionInfo;
import com.codingapi.txlcn.client.support.common.TransactionUnitTypeList;
import com.codingapi.txlcn.client.support.common.cache.TransactionAttachmentCache;
import com.codingapi.txlcn.commons.exception.BeforeBusinessException;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * LCN分布式事务业务执行器
 * Created by lorne on 2017/6/8.
 */
@Component
@Slf4j
public class TXLCNTransactionServiceExecutor {


    @Autowired
    private LCNTransactionBeanHelper lcnTransactionBeanHelper;

    @Autowired
    private TransactionAttachmentCache transactionAttachmentCache;

    @Autowired
    private TxLogger txLogger;

    /**
     * 事务业务执行
     *
     * @param info info
     * @return Object
     * @throws Throwable Throwable
     */
    public Object transactionRunning(TxTransactionInfo info) throws Throwable {

        // 1. 获取事务类型
        String transactionType = info.getTransactionType();

        // 2. 事务状态抉择器
        TXLCNTransactionSeparator lcnTransactionSeparator =
                lcnTransactionBeanHelper.loadLCNTransactionStateResolver(transactionType);

        // 3. 获取事务状态
        TXLCNTransactionState lcnTransactionState = lcnTransactionSeparator.loadTransactionState(info);
        // 3.1 如果不参与分布式事务立即终止
        if (lcnTransactionState.equals(TXLCNTransactionState.NON)) {
            return info.getBusinessCallback().call();
        }

        // 4. 获取bean
        TXLCNTransactionControl lcnTransactionControl =
                lcnTransactionBeanHelper.loadLCNTransactionControl(transactionType, lcnTransactionState);

        // 5. 织入事务操作

        // 5.1 记录事务类型到事务上下文
        transactionAttachmentCache.attach(
                info.getGroupId(), info.getUnitId(), new TransactionUnitTypeList().selfAdd(transactionType));

        try {
            // 5.2 业务执行前
            txLogger.trace(info.getGroupId(), info.getUnitId(), Transactions.TAG_TRANSACTION, "pre service business code");
            lcnTransactionControl.preBusinessCode(info);
            // 5.3 执行业务
            txLogger.trace(info.getGroupId(), info.getUnitId(), Transactions.TAG_TRANSACTION, "do service business code");
            Object result = lcnTransactionControl.doBusinessCode(info);

            // 5.4 业务执行成功
            txLogger.trace(info.getGroupId(), info.getUnitId(), Transactions.TAG_TRANSACTION, "service business success");
            lcnTransactionControl.onBusinessCodeSuccess(info, result);
            return result;
        } catch (BeforeBusinessException e) {
            log.error("business", e);
            throw e;
        } catch (Throwable e) {
            // 5.5 业务执行失败
            txLogger.trace(info.getGroupId(), info.getUnitId(), Transactions.TAG_TRANSACTION, "business code error");
            lcnTransactionControl.onBusinessCodeError(info, e);
            throw e;
        } finally {
            // 5.6 业务执行完毕
            lcnTransactionControl.postBusinessCode(info);
        }
    }


}
