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
package com.codingapi.txlcn.tm.txmsg;

import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.support.TxLcnManagerRpcBeanHelper;
import com.codingapi.txlcn.txmsg.LCNCmdType;
import com.codingapi.txlcn.txmsg.RpcAnswer;
import com.codingapi.txlcn.txmsg.RpcClient;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.dto.RpcCmd;
import com.codingapi.txlcn.txmsg.exception.RpcException;
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
 * @author lorne
 */
@Service
@Slf4j
public class ServerRpcAnswer implements RpcAnswer, DisposableBean {

    private final RpcClient rpcClient;

    private final ExecutorService executorService;

    private final TxLcnManagerRpcBeanHelper rpcBeanHelper;

    @Autowired
    public ServerRpcAnswer(RpcClient rpcClient, TxLcnManagerRpcBeanHelper rpcBeanHelper, TxManagerConfig managerConfig) {
        managerConfig.setConcurrentLevel(
                Math.max(Runtime.getRuntime().availableProcessors() * 5, managerConfig.getConcurrentLevel()));
        this.rpcClient = rpcClient;
        this.executorService = Executors.newFixedThreadPool(managerConfig.getConcurrentLevel(),
                new ThreadFactoryBuilder().setDaemon(false).setNameFormat("tm-rpc-service-%d").build());
        this.rpcBeanHelper = rpcBeanHelper;
    }


    @Override
    public void callback(RpcCmd rpcCmd) {
        executorService.submit(() -> {
            try {
                TransactionCmd transactionCmd = parser(rpcCmd);
                String action = transactionCmd.getMsg().getAction();
                RpcExecuteService rpcExecuteService = rpcBeanHelper.loadManagerService(transactionCmd.getType());
                MessageDto messageDto = null;
                try {
                    Serializable message = rpcExecuteService.execute(transactionCmd);
                    messageDto = MessageCreator.okResponse(message, action);
                } catch (Throwable e) {
                    log.error("rpc execute service error. action: " + action, e);
                    messageDto = MessageCreator.failResponse(e, action);
                } finally {
                    // 对需要响应信息的请求做出响应
                    if (rpcCmd.getKey() != null) {
                        assert Objects.nonNull(messageDto);
                        try {
                            messageDto.setGroupId(rpcCmd.getMsg().getGroupId());
                            rpcCmd.setMsg(messageDto);
                            rpcClient.send(rpcCmd);
                        } catch (RpcException ignored) {
                        }
                    }
                }
            } catch (Throwable e) {
                if (rpcCmd.getKey() != null) {
                    log.info("send response.");
                    String action = rpcCmd.getMsg().getAction();
                    // 事务协调器业务未处理的异常响应服务器失败
                    rpcCmd.setMsg(MessageCreator.serverException(action));
                    try {
                        rpcClient.send(rpcCmd);
                        log.info("send response ok.");
                    } catch (RpcException ignored) {
                        log.error("requester:{} dead.", rpcCmd.getRemoteKey());
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

    private TransactionCmd parser(RpcCmd rpcCmd) {
        TransactionCmd cmd = new TransactionCmd();
        cmd.setRequestKey(rpcCmd.getKey());
        cmd.setRemoteKey(rpcCmd.getRemoteKey());
        cmd.setType(LCNCmdType.parserCmd(rpcCmd.getMsg().getAction()));
        cmd.setGroupId(rpcCmd.getMsg().getGroupId());
        cmd.setMsg(rpcCmd.getMsg());
        return cmd;
    }

}
