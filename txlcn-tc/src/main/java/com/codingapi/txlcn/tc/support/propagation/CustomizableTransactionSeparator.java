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
package com.codingapi.txlcn.tc.support.propagation;

import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.TxTransactionInfo;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.common.exception.TransactionException;
import com.codingapi.txlcn.tc.core.DTXState;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: 可定制的事务分离器
 * Date: 2018/12/5
 *
 * @author ujued
 */
@Slf4j
public class CustomizableTransactionSeparator implements TXLCNTransactionSeparator {

    @Override
    public DTXState loadTransactionState(TxTransactionInfo txTransactionInfo) throws TransactionException {

        // 本线程已经参与分布式事务(本地方法互调)
        if (DTXLocalContext.cur().isInUnit()) {
            log.info("Default by DTXLocalContext is not null! {}", DTXLocalContext.cur());
            return DTXState.DEFAULT;
        }

        // 发起分布式事务条件
        if (txTransactionInfo.isTransactionStart()) {
            // 发起方时，对于只加入DTX的事务单元走默认处理
            if (DTXPropagation.SUPPORTS.equals(txTransactionInfo.getPropagation())) {
                return DTXState.NON;
            }
            return DTXState.STARTING;
        }

        // 加入分布式事务
        return DTXState.RUNNING;
    }
}
