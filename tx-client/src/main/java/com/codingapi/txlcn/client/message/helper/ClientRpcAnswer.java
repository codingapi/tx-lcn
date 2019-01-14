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
package com.codingapi.txlcn.client.message.helper;

import com.codingapi.txlcn.spi.message.RpcAnswer;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.dto.RpcCmd;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.client.support.LCNTransactionBeanHelper;
import com.codingapi.txlcn.commons.exception.TxClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Description: TxClient对RPC命令回复
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Service
@Slf4j
public class ClientRpcAnswer implements RpcAnswer {

    private final LCNTransactionBeanHelper transactionBeanHelper;

    private final RpcClient rpcClient;

    @Autowired
    public ClientRpcAnswer(LCNTransactionBeanHelper transactionBeanHelper, RpcClient rpcClient) {
        this.transactionBeanHelper = transactionBeanHelper;
        this.rpcClient = rpcClient;
    }

    @Override
    public void callback(RpcCmd rpcCmd) {
        log.debug("Receive Message: {}", rpcCmd.getMsg());
        TransactionCmd transactionCmd = MessageParser.parser(rpcCmd);
        String transactionType = transactionCmd.getTransactionType();
        String action = transactionCmd.getMsg().getAction();
        RpcExecuteService executeService =
                transactionBeanHelper.loadRpcExecuteService(transactionType, transactionCmd.getType());
        MessageDto messageDto = null;
        try {
            Object message = executeService.execute(transactionCmd);
            messageDto = MessageCreator.notifyUnitOkResponse(message,action);
        } catch (TxClientException e) {
            log.error("message > execute error.", e);
            messageDto = MessageCreator.notifyUnitFailResponse(e,action);
        } finally {
            if (Objects.nonNull(rpcCmd.getKey())) {
                try {
                    rpcCmd.setMsg(messageDto);
                    rpcClient.send(rpcCmd);
                } catch (RpcException e) {
                    log.error("response request[{}] error. error message: {}", rpcCmd.getKey(), e.getMessage());
                }
            }
        }
    }
}
