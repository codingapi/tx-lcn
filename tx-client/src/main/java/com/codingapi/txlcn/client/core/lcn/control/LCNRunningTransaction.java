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
package com.codingapi.txlcn.client.core.lcn.control;

import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.commons.exception.TransactionClearException;
import com.codingapi.txlcn.spi.sleuth.TracerHelper;
import com.codingapi.txlcn.client.bean.TxTransactionInfo;
import com.codingapi.txlcn.client.support.TXLCNTransactionControl;
import com.codingapi.txlcn.client.support.common.cache.TransactionAttachmentCache;
import com.codingapi.txlcn.client.support.common.template.TransactionCleanTemplate;
import com.codingapi.txlcn.client.support.common.template.TransactionControlTemplate;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.commons.exception.TxClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Description:
 * Date: 2018/12/3
 *
 * @author ujued
 */
@Component("control_lcn_running")
@Slf4j
public class LCNRunningTransaction implements TXLCNTransactionControl {

    private final TracerHelper tracerHelper;

    private final TransactionAttachmentCache transactionAttachmentCache;

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final TransactionControlTemplate transactionControlTemplate;

    @Autowired
    public LCNRunningTransaction(TransactionAttachmentCache transactionAttachmentCache,
                                 TracerHelper tracerHelper,
                                 TransactionCleanTemplate transactionCleanTemplate,
                                 TransactionControlTemplate transactionControlTemplate) {
        this.transactionAttachmentCache = transactionAttachmentCache;
        this.tracerHelper = tracerHelper;
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.transactionControlTemplate = transactionControlTemplate;
    }


    @Override
    public void preBusinessCode(TxTransactionInfo info) {
        // LCN 类型事务需要代理资源
        DTXLocal.makeProxy();
    }


    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        try {
            transactionCleanTemplate.clean(info.getGroupId(), info.getUnitId(), info.getTransactionType(), 0);
        } catch (TransactionClearException e) {
            log.error("{} > clean transaction error.", Transactions.LCN);
        }
    }


    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) throws TxClientException {
        log.debug("join group: [GroupId: {}, TxManager:{}, Method: {}]", info.getGroupId(),
                Optional.ofNullable(tracerHelper.getTxManagerKey()).orElseThrow(() -> new RuntimeException("sleuth pass error.")),
                info.getTransactionInfo().getMethodStr());

        // 加入事务组
        transactionControlTemplate.joinGroup(info.getGroupId(), info.getUnitId(), info.getTransactionType(),
                info.getTransactionInfo());
    }

}
