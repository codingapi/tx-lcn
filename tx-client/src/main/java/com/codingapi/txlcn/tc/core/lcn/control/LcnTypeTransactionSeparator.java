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
package com.codingapi.txlcn.tc.core.lcn.control;

import com.codingapi.txlcn.commons.exception.TransactionException;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.tc.core.DTXState;
import com.codingapi.txlcn.tc.core.TxTransactionInfo;
import com.codingapi.txlcn.tc.support.context.TCGlobalContext;
import com.codingapi.txlcn.tc.support.propagation.CustomizableTransactionSeparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/5
 *
 * @author ujued
 */
@Slf4j
@Component("transaction_state_resolver_lcn")
public class LcnTypeTransactionSeparator extends CustomizableTransactionSeparator {

    private final TCGlobalContext globalContext;

    @Autowired
    public LcnTypeTransactionSeparator(TCGlobalContext globalContext) {
        this.globalContext = globalContext;
    }

    @Override
    public DTXState loadTransactionState(TxTransactionInfo txTransactionInfo) throws TransactionException {
        // if has more one lcn type use default
        if (globalContext.dtxContext(txTransactionInfo.getGroupId()).getTransactionTypes().contains(Transactions.LCN)) {
            log.info("Default by LCN assert !");
            return DTXState.DEFAULT;
        }
        return super.loadTransactionState(txTransactionInfo);
    }
}
