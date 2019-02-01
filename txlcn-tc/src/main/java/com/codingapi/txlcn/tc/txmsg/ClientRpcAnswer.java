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
package com.codingapi.txlcn.tc.txmsg;

import com.codingapi.txlcn.txmsg.RpcAnswer;
import com.codingapi.txlcn.txmsg.RpcClient;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.dto.RpcCmd;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import com.codingapi.txlcn.tc.support.TxLcnBeanHelper;
import com.codingapi.txlcn.common.exception.TxClientException;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Description: TxClient对RPC命令回复
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Service
@Slf4j
public class ClientRpcAnswer implements RpcAnswer, DisposableBean {

    private final TxLcnBeanHelper transactionBeanHelper;

    private final RpcClient rpcClient;

    private final ExecutorService executorService;

    @Autowired
    public ClientRpcAnswer(TxLcnBeanHelper transactionBeanHelper, RpcClient rpcClient) {
        this.transactionBeanHelper = transactionBeanHelper;
        this.rpcClient = rpcClient;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 5,
                new ThreadFactoryBuilder().setDaemon(false).setNameFormat("tc-rpc-service-%d").build());
    }

    @Override
    public void callback(RpcCmd rpcCmd) {
        executorService.submit(() -> {
            log.debug("Receive Message: {}", rpcCmd.getMsg());
            TransactionCmd transactionCmd = MessageParser.parser(rpcCmd);
            String transactionType = transactionCmd.getTransactionType();
            String action = transactionCmd.getMsg().getAction();
            RpcExecuteService executeService =
                    transactionBeanHelper.loadRpcExecuteService(transactionType, transactionCmd.getType());
            MessageDto messageDto = null;
            try {
                Serializable message = executeService.execute(transactionCmd);
                messageDto = MessageCreator.notifyUnitOkResponse(message, action);
            } catch (TxClientException e) {
                log.error("message > execute error.", e);
                messageDto = MessageCreator.notifyUnitFailResponse(e, action);
            } catch (Throwable e) {
                e.printStackTrace();
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
        });
    }

    @Override
    public void destroy() throws Exception {
        this.executorService.shutdown();
        this.executorService.awaitTermination(6, TimeUnit.SECONDS);
    }
}
