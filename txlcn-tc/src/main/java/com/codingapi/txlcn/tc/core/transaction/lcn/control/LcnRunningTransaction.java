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

import com.codingapi.txlcn.common.exception.TransactionClearException;
import com.codingapi.txlcn.common.exception.TransactionException;
import com.codingapi.txlcn.common.exception.TxClientException;
import com.codingapi.txlcn.common.util.Transactions;
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
 * Date: 2018/12/3
 *
 * @author ujued
 */
@Component("control_lcn_running")
@Slf4j
public class LcnRunningTransaction implements DTXLocalControl {
    
    private final TransactionCleanTemplate transactionCleanTemplate;
    
    private final TransactionControlTemplate transactionControlTemplate;
    
    @Autowired
    public LcnRunningTransaction(TransactionCleanTemplate transactionCleanTemplate,
                                 TransactionControlTemplate transactionControlTemplate) {
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.transactionControlTemplate = transactionControlTemplate;
    }
    
    
    @Override
    public void preBusinessCode(TxTransactionInfo info) {
        // lcn type need connection proxy
        DTXLocalContext.makeProxy();
    }
    
    
    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        try {
            transactionCleanTemplate.clean(info.getGroupId(), info.getUnitId(), info.getTransactionType(), 0);
        } catch (TransactionClearException e) {
            log.error("{} > clean transaction error." , Transactions.LCN);
        }
    }
    
    
    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) throws TransactionException {
        log.debug("join group: [GroupId: {},Method: {}]" , info.getGroupId(),
                info.getTransactionInfo().getMethodStr());
        
        // join DTX group
        transactionControlTemplate.joinGroup(info.getGroupId(), info.getUnitId(), info.getTransactionType(),
                info.getTransactionInfo());
    }
    
}
