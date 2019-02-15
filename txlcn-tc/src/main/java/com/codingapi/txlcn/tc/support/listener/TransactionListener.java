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
package com.codingapi.txlcn.tc.support.listener;

import com.codingapi.txlcn.tc.core.TxTransactionInfo;

/**
 * Description: 事务流程监听器
 * Date: 19-1-25 下午1:27
 *
 * @author ujued
 */
public interface TransactionListener {
    
    /**
     * tx begin
     *
     * @param txTransactionInfo txTransactionInfo
     */
    void onTransactionBegin(TxTransactionInfo txTransactionInfo);
    
    /**
     * tx error
     *
     * @param txTransactionInfo txTransactionInfo
     */
    void onTransactionError(TxTransactionInfo txTransactionInfo);
    
    /**
     * tx clean
     *
     * @param groupId          groupId
     * @param unitId           unitId
     * @param transactionState transactionState
     */
    void onTransactionClean(String groupId, String unitId, int transactionState);
}
