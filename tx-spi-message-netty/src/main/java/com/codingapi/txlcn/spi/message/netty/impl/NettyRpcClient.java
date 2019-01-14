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
package com.codingapi.txlcn.spi.message.netty.impl;

import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.dto.RpcCmd;
import com.codingapi.txlcn.spi.message.dto.RpcResponseState;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.netty.SocketManager;
import com.codingapi.txlcn.spi.message.netty.bean.NettyRpcCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Component
@Slf4j
public class NettyRpcClient extends RpcClient {


    @Override
    public RpcResponseState send(RpcCmd rpcCmd) throws RpcException {
        return SocketManager.getInstance().send(rpcCmd.getRemoteKey(), rpcCmd);
    }


    @Override
    public RpcResponseState send(String remoteKey, MessageDto msg) throws RpcException {
        RpcCmd rpcCmd = new NettyRpcCmd();
        rpcCmd.setMsg(msg);
        rpcCmd.setRemoteKey(remoteKey);
        return send(rpcCmd);
    }

    @Override
    public MessageDto request(RpcCmd rpcCmd) throws RpcException {
        if (rpcCmd.getKey() == null) {
            throw new RpcException("key not null.");
        }
        return SocketManager.getInstance().request(rpcCmd.getRemoteKey(), rpcCmd);
    }

    @Override
    public MessageDto request(String remoteKey, MessageDto msg) throws RpcException {
        long startTime = System.currentTimeMillis();
        NettyRpcCmd rpcCmd = new NettyRpcCmd();
        rpcCmd.setMsg(msg);
        String key = rpcCmd.randomKey();
        rpcCmd.setKey(key);
        rpcCmd.setRemoteKey(remoteKey);
        MessageDto result = request(rpcCmd);
        log.debug("cmd request used time: {} ms", System.currentTimeMillis() - startTime);
        return result;
    }


    @Override
    public List<String> loadAllRemoteKey() {
        return SocketManager.getInstance().loadAllRemoteKey();
    }


    @Override
    public List<String> moduleList(String moduleName) {
        return SocketManager.getInstance().moduleList(moduleName);
    }


    @Override
    public void bindAppName(String remoteKey, String appName)   {
        SocketManager.getInstance().bindModuleName(remoteKey,appName);
    }

    @Override
    public String getAppName(String remoteKey)  {
        return  SocketManager.getInstance().getModuleName(remoteKey);
    }
}
