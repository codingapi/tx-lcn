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
package com.codingapi.txlcn.tm.support.service;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.common.exception.TransactionStateException;
import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.tm.support.restapi.ao.WriteTxExceptionDTO;
import com.codingapi.txlcn.tm.support.restapi.vo.ExceptionList;

import java.util.List;

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
     * @param groupId groupId
     * @return transactionState
     */
    int transactionState(String groupId);

    /**
     * 获取补偿列表
     *
     * @param page      page
     * @param limit     limit
     * @param exState   exState
     * @param keyword   keyword
     * @param registrar registrar
     * @return ExceptionList
     */
    ExceptionList exceptionList(Integer page, Integer limit, Integer exState, String keyword, Integer registrar);

    /**
     * Client 切面信息
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @return JSONObject
     * @throws TxManagerException        TxManagerException
     * @throws TransactionStateException TransactionStateException
     */
    JSONObject getTransactionInfo(String groupId, String unitId) throws TxManagerException, TransactionStateException;

    /**
     * 删除异常
     *
     * @param ids 异常标识
     * @throws TxManagerException ex
     */
    void deleteExceptions(List<Long> ids) throws TxManagerException;

    /**
     * 删除异常事务信息
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @param modId   modId
     * @throws TxManagerException TxManagerException
     */
    void deleteTransactionInfo(String groupId, String unitId, String modId) throws TxManagerException;
}
