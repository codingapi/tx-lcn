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
package com.codingapi.txlcn.tc.core;

import com.codingapi.txlcn.common.exception.TransactionException;

/**
 *  LCN分布式事务控制
 * @author lorne
 */
public interface DTXLocalControl {

    /**
     * 业务代码执行前
     *
     * @param info info
     * @throws  TransactionException TransactionException
     */
    default void preBusinessCode(TxTransactionInfo info) throws TransactionException {

    }

    /**
     * 执行业务代码
     *
     * @param info info
     * @return  Object Object
     * @throws Throwable Throwable
     */
    default Object doBusinessCode(TxTransactionInfo info) throws Throwable {
        return info.getBusinessCallback().call();
    }


    /**
     * 业务代码执行失败
     *
     * @param info info
     * @param throwable throwable
     * @throws TransactionException TransactionException
     */
    default void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) throws TransactionException {

    }

    /**
     * 业务代码执行成功
     *
     * @param info info
     * @param result result
     * @throws TransactionException TransactionException
     */
    default void onBusinessCodeSuccess(TxTransactionInfo info, Object result) throws TransactionException {

    }

    /**
     * 清场
     *
     * @param info info
     */
    default void postBusinessCode(TxTransactionInfo info) {

    }
}
