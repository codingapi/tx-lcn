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
import com.codingapi.txlcn.commons.exception.BeforeBusinessException;
import com.codingapi.txlcn.commons.exception.TxClientException;

/**
 *  LCN分布式事务控制
 * @author lorne
 */
public interface TXLCNTransactionControl {

    /**
     * 业务代码执行前
     *
     * @param info info
     * @throws  BeforeBusinessException BeforeBusinessException
     */
    default void preBusinessCode(TxTransactionInfo info) throws BeforeBusinessException {

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
     */
    default void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {

    }

    /**
     * 业务代码执行成功
     *
     * @param info info
     * @param result result
     * @throws TxClientException TxClientException
     */
    default void onBusinessCodeSuccess(TxTransactionInfo info, Object result) throws TxClientException {

    }

    /**
     * 清场
     *
     * @param info info
     */
    default void postBusinessCode(TxTransactionInfo info) {

    }
}
