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
package com.codingapi.txlcn.manager.support.service;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.commons.exception.TransactionStateException;
import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.manager.support.restapi.model.ExceptionList;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
public interface TxExceptionService {

    /**
     * 写补偿记录
     *
     * @param writeTxExceptionReq writeTxExceptionReq
     */
    void writeTxException(WriteTxExceptionDTO writeTxExceptionReq);


    /**
     * 获取事务状态
     *
     * @param  groupId groupId
     * @return transactionState
     */
    int transactionState(String groupId);

    /**
     * 获取补偿列表
     *
     * @param page page
     * @param limit limit
     * @param keyword  keyword
     * @param registrar  registrar
     * @return ExceptionList
     */
    ExceptionList exceptionList(Integer page, Integer limit, String keyword, int registrar);

    /**
     * Client 切面信息
     *
     * @param groupId groupId
     * @param unitId unitId
     * @return JSONObject
     * @throws TxManagerException TxManagerException
     * @throws TransactionStateException TransactionStateException
     */
    JSONObject getTransactionInfo(String groupId, String unitId) throws TxManagerException, TransactionStateException;
}
