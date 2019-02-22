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
package com.codingapi.txlcn.tm.txmsg.transaction;

import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.txmsg.RpcClient;
import com.codingapi.txlcn.txmsg.params.TxExceptionParams;
import com.codingapi.txlcn.tm.core.TransactionManager;
import com.codingapi.txlcn.tm.txmsg.RpcExecuteService;
import com.codingapi.txlcn.tm.txmsg.TransactionCmd;
import com.codingapi.txlcn.tm.support.service.TxExceptionService;
import com.codingapi.txlcn.tm.support.restapi.ao.WriteTxExceptionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/20
 *
 * @author ujued
 */
@Component("rpc_write-exception")
@Slf4j
public class WriteTxExceptionExecuteService implements RpcExecuteService {

    private final TxExceptionService compensationService;

    private final RpcClient rpcClient;

    private final TransactionManager transactionManager;

    @Autowired
    public WriteTxExceptionExecuteService(TxExceptionService compensationService, RpcClient rpcClient,
                                          TransactionManager transactionManager) {
        this.compensationService = compensationService;
        this.rpcClient = rpcClient;
        this.transactionManager = transactionManager;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            TxExceptionParams txExceptionParams = transactionCmd.getMsg().loadBean(TxExceptionParams.class);
            WriteTxExceptionDTO writeTxExceptionReq = new WriteTxExceptionDTO();
            writeTxExceptionReq.setModId(rpcClient.getAppName(transactionCmd.getRemoteKey()));

            //获取事务状态（可能存在设置了手动回滚）
            int transactionState = transactionManager.transactionStateFromFastStorage(transactionCmd.getGroupId());

            writeTxExceptionReq.setTransactionState(transactionState == -1 ? txExceptionParams.getTransactionState() : transactionState);
            writeTxExceptionReq.setGroupId(txExceptionParams.getGroupId());
            writeTxExceptionReq.setUnitId(txExceptionParams.getUnitId());
            writeTxExceptionReq.setRegistrar(Objects.isNull(txExceptionParams.getRegistrar()) ? -1 : txExceptionParams.getRegistrar());
            writeTxExceptionReq.setRemark(txExceptionParams.getRemark());
            compensationService.writeTxException(writeTxExceptionReq);
        } catch (Exception e) {
            throw new TxManagerException(e);
        }
        return null;
    }
}
