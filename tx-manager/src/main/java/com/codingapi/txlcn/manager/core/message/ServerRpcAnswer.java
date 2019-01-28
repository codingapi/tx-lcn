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
package com.codingapi.txlcn.manager.core.message;

import com.codingapi.txlcn.spi.message.RpcAnswer;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.RpcCmd;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.logger.TxLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lorne
 */
@Service
@Slf4j
public class ServerRpcAnswer implements RpcAnswer {

    private final HashGroupRpcCmdHandler hashGroupRpcCmdHandler;

    private final RpcClient rpcClient;

    @Autowired
    private TxLogger txLogger;

    @Autowired
    public ServerRpcAnswer(HashGroupRpcCmdHandler hashGroupRpcCmdHandler, RpcClient rpcClient) {
        this.hashGroupRpcCmdHandler = hashGroupRpcCmdHandler;
        this.rpcClient = rpcClient;
    }


    @Override
    public void callback(RpcCmd rpcCmd) {

        txLogger.trace(rpcCmd.getMsg().getGroupId(),"","rpccmd",rpcCmd.getMsg().getAction());

        try {
            hashGroupRpcCmdHandler.handleMessage(rpcCmd);
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
    }
}
