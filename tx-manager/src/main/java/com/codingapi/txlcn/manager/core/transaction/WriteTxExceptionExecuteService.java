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
package com.codingapi.txlcn.manager.core.transaction;

import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.manager.support.service.TxExceptionService;
import com.codingapi.txlcn.manager.support.service.WriteTxExceptionDTO;
import com.codingapi.txlcn.manager.core.message.RpcExecuteService;
import com.codingapi.txlcn.manager.core.message.TransactionCmd;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.params.TxExceptionParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/20
 *
 * @author ujued
 */
@Component("rpc_write-compensation")
@Slf4j
public class WriteTxExceptionExecuteService implements RpcExecuteService {

    private final TxExceptionService compensationService;

    private final RpcClient rpcClient;

    @Autowired
    public WriteTxExceptionExecuteService(TxExceptionService compensationService, RpcClient rpcClient) {
        this.compensationService = compensationService;
        this.rpcClient = rpcClient;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            TxExceptionParams txExceptionParams = transactionCmd.getMsg().loadData(TxExceptionParams.class);
            WriteTxExceptionDTO writeTxExceptionReq = new WriteTxExceptionDTO();
            writeTxExceptionReq.setModId(rpcClient.getAppName(transactionCmd.getRemoteKey()));
            writeTxExceptionReq.setTransactionState(txExceptionParams.getTransactionState());
            writeTxExceptionReq.setGroupId(txExceptionParams.getGroupId());
            writeTxExceptionReq.setUnitId(txExceptionParams.getUnitId());
            writeTxExceptionReq.setRegistrar(Objects.isNull(txExceptionParams.getRegistrar()) ? -1 : txExceptionParams.getRegistrar());
            compensationService.writeTxException(writeTxExceptionReq);
        } catch (SerializerException e) {
            throw new TxManagerException(e);
        }
        return null;
    }
}
