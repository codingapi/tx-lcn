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
package com.codingapi.txlcn.tc.core.transaction.lcn.control;

import com.codingapi.txlcn.common.exception.TransactionException;
import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.TxTransactionInfo;
import com.codingapi.txlcn.tc.core.DTXLocalControl;
import com.codingapi.txlcn.tc.core.context.TCGlobalContext;
import com.codingapi.txlcn.tc.core.template.TransactionControlTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lorne
 */
@Service(value = "control_lcn_starting")
@Slf4j
public class LcnStartingTransaction implements DTXLocalControl {

    private final TransactionControlTemplate transactionControlTemplate;

    private final TCGlobalContext globalContext;


    @Autowired
    public LcnStartingTransaction(TransactionControlTemplate transactionControlTemplate, TCGlobalContext globalContext) {
        this.transactionControlTemplate = transactionControlTemplate;
        this.globalContext = globalContext;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) throws TransactionException {
        // create DTX group
        transactionControlTemplate.createGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionInfo(), info.getTransactionType());

        // lcn type need connection proxy
        DTXLocalContext.makeProxy();
    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        DTXLocalContext.cur().setSysTransactionState(0);
    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) {
        DTXLocalContext.cur().setSysTransactionState(1);
    }

    @Override
    public void postBusinessCode(TxTransactionInfo info) {
        // RPC close DTX group
        transactionControlTemplate.notifyGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionType(),
                DTXLocalContext.transactionState(globalContext.dtxState(info.getGroupId())));
    }
}
