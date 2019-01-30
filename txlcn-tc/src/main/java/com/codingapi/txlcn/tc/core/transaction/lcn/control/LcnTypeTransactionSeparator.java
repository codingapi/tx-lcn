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
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tc.core.DTXLogicState;
import com.codingapi.txlcn.tc.core.TxTransactionInfo;
import com.codingapi.txlcn.tc.core.context.TCGlobalContext;
import com.codingapi.txlcn.tc.core.propagation.CustomizableTransactionSeparator;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Description:
 * Date: 2018/12/5
 *
 * @author ujued
 */
@Component("transaction_state_resolver_lcn")
public class LcnTypeTransactionSeparator extends CustomizableTransactionSeparator {

    private final TCGlobalContext globalContext;

    private final TxLogger txLogger;

    @Autowired
    public LcnTypeTransactionSeparator(TCGlobalContext globalContext, TxLogger txLogger) {
        this.globalContext = globalContext;
        this.txLogger = txLogger;
    }

    @Override
    public DTXLogicState loadTransactionState(TxTransactionInfo txTransactionInfo) throws TransactionException {
        // 一个模块存在多个LCN类型的事务单元在一个事务内走DEFAULT
        Set<String> transactionTypeSet = globalContext.txContext(txTransactionInfo.getGroupId()).getTransactionTypes();
        boolean isDefault = transactionTypeSet.contains(Transactions.LCN);
        txLogger.info(txTransactionInfo.getGroupId(), txTransactionInfo.getUnitId(), Transactions.LCN,
                "having types: %s, is default: ", transactionTypeSet, isDefault);
        if (isDefault) {
            txLogger.info(txTransactionInfo.getGroupId(), txTransactionInfo.getUnitId(), Transactions.LCN,
                    "Default DTXLogicState by LCN assert !");
            return DTXLogicState.DEFAULT;
        }
        return super.loadTransactionState(txTransactionInfo);
    }

    public static void main(String[] args) {
        Set<String> hashSet = Sets.newHashSet("lcn");
        System.out.println(hashSet);
        System.out.println(hashSet.contains(Transactions.LCN));
    }
}
