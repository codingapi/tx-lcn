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
package com.codingapi.txlcn.txmsg.netty.impl;

import com.codingapi.txlcn.txmsg.RpcClient;
import com.codingapi.txlcn.txmsg.dto.AppInfo;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.dto.RpcCmd;
import com.codingapi.txlcn.txmsg.dto.RpcResponseState;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import com.codingapi.txlcn.txmsg.netty.bean.SocketManager;
import com.codingapi.txlcn.txmsg.netty.bean.NettyRpcCmd;
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
        return request0(rpcCmd, -1);
    }

    private MessageDto request0(RpcCmd rpcCmd, long timeout) throws RpcException {
        if (rpcCmd.getKey() == null) {
            throw new RpcException("key must be not null.");
        }
        return SocketManager.getInstance().request(rpcCmd.getRemoteKey(), rpcCmd, timeout);
    }

    @Override
    public MessageDto request(String remoteKey, MessageDto msg) throws RpcException {
        return request(remoteKey, msg, -1);
    }

    @Override
    public MessageDto request(String remoteKey, MessageDto msg, long timeout) throws RpcException {
        long startTime = System.currentTimeMillis();
        NettyRpcCmd rpcCmd = new NettyRpcCmd();
        rpcCmd.setMsg(msg);
        String key = rpcCmd.randomKey();
        rpcCmd.setKey(key);
        rpcCmd.setRemoteKey(remoteKey);
        MessageDto result = request0(rpcCmd, timeout);
        log.debug("cmd request used time: {} ms", System.currentTimeMillis() - startTime);
        return result;
    }


    @Override
    public List<String> loadAllRemoteKey() {
        return SocketManager.getInstance().loadAllRemoteKey();
    }


    @Override
    public List<String> remoteKeys(String moduleName) {
        return SocketManager.getInstance().remoteKeys(moduleName);
    }


    @Override
    public void bindAppName(String remoteKey, String appName,String labelName) throws RpcException {
        SocketManager.getInstance().bindModuleName(remoteKey, appName,labelName);
    }

    @Override
    public String getAppName(String remoteKey) {
        return SocketManager.getInstance().getModuleName(remoteKey);
    }

    @Override
    public List<AppInfo> apps() {
        return SocketManager.getInstance().appInfos();
    }
}
