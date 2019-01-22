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
package com.codingapi.txlcn.tc.core.tcc.control;

import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.TxTransactionInfo;
import com.codingapi.txlcn.tc.core.tcc.TccTransactionInfoCache;
import com.codingapi.txlcn.tc.core.DTXLocalControl;
import com.codingapi.txlcn.tc.support.template.TransactionCleanTemplate;
import com.codingapi.txlcn.tc.support.template.TransactionControlTemplate;
import com.codingapi.txlcn.commons.exception.BeforeBusinessException;
import com.codingapi.txlcn.commons.exception.TransactionClearException;
import com.codingapi.txlcn.commons.exception.TxClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 侯存路
 */
@Service(value = "control_tcc_running")
@Slf4j
public class TccRunningTransaction implements DTXLocalControl {

    private final TccTransactionInfoCache tccTransactionInfoCache;

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final TransactionControlTemplate transactionControlTemplate;

    @Autowired
    public TccRunningTransaction(TransactionCleanTemplate transactionCleanTemplate,
                                 TransactionControlTemplate transactionControlTemplate,
                                 TccTransactionInfoCache tccTransactionInfoCache) {
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.transactionControlTemplate = transactionControlTemplate;
        this.tccTransactionInfoCache = tccTransactionInfoCache;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) throws BeforeBusinessException {

        // 缓存TCC事务信息，如果有必要
        if (tccTransactionInfoCache.get(info.getUnitId()) == null) {
            tccTransactionInfoCache.putIfAbsent(info.getUnitId(), TccStartingTransaction.prepareTccInfo(info));
        }

        tccTransactionInfoCache.get(info.getUnitId()).setMethodParameter(info.getTransactionInfo().getArgumentValues());
    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        try {
            transactionCleanTemplate.clean(
                    DTXLocalContext.cur().getGroupId(),
                    info.getUnitId(),
                    info.getTransactionType(),
                    0);
        } catch (TransactionClearException e) {
            log.error("tcc > clean transaction error.", e);
        }
    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) throws TxClientException {
        transactionControlTemplate.joinGroup(info.getGroupId(), info.getUnitId(), info.getTransactionType(),
                info.getTransactionInfo());
    }

}
