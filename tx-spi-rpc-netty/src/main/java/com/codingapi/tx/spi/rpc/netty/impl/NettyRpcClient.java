package com.codingapi.tx.spi.rpc.netty.impl;

import com.codingapi.tx.spi.rpc.RpcClient;
import com.codingapi.tx.spi.rpc.dto.MessageDto;
import com.codingapi.tx.spi.rpc.dto.RpcCmd;
import com.codingapi.tx.spi.rpc.dto.RpcResponseState;
import com.codingapi.tx.spi.rpc.exception.RpcException;
import com.codingapi.tx.spi.rpc.netty.SocketManager;
import com.codingapi.tx.spi.rpc.netty.bean.NettyRpcCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Service
@Slf4j
public class NettyRpcClient implements RpcClient {


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
        log.debug("rpc > request > used time: {} ms", System.currentTimeMillis() - startTime);
        return result;
    }

    @Override
    public String loadRemoteKey() throws RpcException {
        return SocketManager.getInstance().loadRemoteKey();
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
    public void bindAppName(String remoteKey, String appName) throws RpcException {
        SocketManager.getInstance().bindModuleName(remoteKey,appName);
    }

    @Override
    public String getAppName(String remoteKey) throws RpcException {
        return  SocketManager.getInstance().getModuleName(remoteKey);
    }
}
