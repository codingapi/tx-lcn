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
package com.codingapi.txlcn.txmsg.netty.bean;


import com.codingapi.txlcn.txmsg.RpcConfig;
import com.codingapi.txlcn.txmsg.dto.AppInfo;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.dto.RpcCmd;
import com.codingapi.txlcn.txmsg.dto.RpcResponseState;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by lorne on 2017/6/30.
 */
@Slf4j
public class SocketManager {

    private Map<String, AppInfo> appNames;

    private ScheduledExecutorService executorService;

    private ChannelGroup channels;

    private static SocketManager manager = null;

    private long attrDelayTime = 1000 * 60;

    private SocketManager() {
        channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        appNames = new ConcurrentHashMap<>();
        executorService = Executors.newSingleThreadScheduledExecutor();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdown();
            try {
                executorService.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException ignored) {
            }
        }));
    }


    public static SocketManager getInstance() {
        if (manager == null) {
            synchronized (SocketManager.class) {
                if (manager == null) {
                    manager = new SocketManager();
                }
            }
        }
        return manager;
    }


    public void addChannel(Channel channel) {
        channels.add(channel);
    }

    public void removeChannel(Channel channel) {
        channels.remove(channel);
        String key = channel.remoteAddress().toString();

        // 未设置过期时间，立即过期
        if (attrDelayTime < 0) {
            appNames.remove(key);
            return;
        }

        // 设置了过期时间，到时间后清除
        try {
            executorService.schedule(() -> {
                appNames.remove(key);
            }, attrDelayTime, TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException ignored) {
            // caused down server.
        }
    }


    private Channel getChannel(String key) throws RpcException {
        for (Channel channel : channels) {
            String val = channel.remoteAddress().toString();
            if (key.equals(val)) {
                return channel;
            }
        }
        throw new RpcException("channel not online.");
    }


    public RpcResponseState send(String key, RpcCmd cmd) throws RpcException {
        Channel channel = getChannel(key);
        ChannelFuture future = channel.writeAndFlush(cmd).syncUninterruptibly();
        return future.isSuccess() ? RpcResponseState.success : RpcResponseState.fail;
    }

    public MessageDto request(String key, RpcCmd cmd, long timeout) throws RpcException {
        NettyRpcCmd nettyRpcCmd = (NettyRpcCmd) cmd;
        log.debug("get channel, key:{}", key);
        Channel channel = getChannel(key);
        channel.writeAndFlush(nettyRpcCmd);
        log.debug("await response");
        if (timeout < 1) {
            nettyRpcCmd.await();
        } else {
            nettyRpcCmd.await(timeout);
        }
        MessageDto res = cmd.loadResult();
        log.debug("response is: {}", res);
        nettyRpcCmd.loadRpcContent().clear();
        return res;
    }

    public MessageDto request(String key, RpcCmd cmd) throws RpcException {
        return request(key, cmd, -1);
    }


    public List<String> loadAllRemoteKey() {
        List<String> allKeys = new ArrayList<>();
        for (Channel channel : channels) {
            allKeys.add(channel.remoteAddress().toString());
        }
        return allKeys;
    }

    public ChannelGroup getChannels() {
        return channels;
    }

    public int currentSize() {
        return channels.size();
    }


    public boolean noConnect(SocketAddress socketAddress) {
        for (Channel channel : channels) {
            if (channel.remoteAddress().toString().equals(socketAddress.toString())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取模块的远程标识keys
     *
     * @param moduleName 模块名称
     * @return remoteKeys
     */
    public List<String> remoteKeys(String moduleName) {
        List<String> allKeys = new ArrayList<>();
        for (Channel channel : channels) {
            if (moduleName.equals(getModuleName(channel))) {
                allKeys.add(channel.remoteAddress().toString());
            }
        }
        return allKeys;
    }


    /**
     * 绑定连接数据
     *
     * @param remoteKey  远程标识
     * @param appName  模块名称
     * @param labelName TC标识名称
     */
    public void bindModuleName(String remoteKey, String appName,String labelName) throws RpcException{
        AppInfo appInfo = new AppInfo();
        appInfo.setAppName(appName);
        appInfo.setLabelName(labelName);
        appInfo.setCreateTime(new Date());
        if(containsLabelName(labelName)){
            throw new RpcException("labelName:"+labelName+" has exist.");
        }
        appNames.put(remoteKey, appInfo);
    }

    public boolean containsLabelName(String moduleName){
        Set<String> keys =  appNames.keySet();
        for(String key:keys){
            AppInfo appInfo = appNames.get(key);
            if(moduleName.equals(appInfo.getAppName())){
                return true;
            }
        }
        return false;
    }

    public void setRpcConfig(RpcConfig rpcConfig) {
        attrDelayTime = rpcConfig.getAttrDelayTime();
    }

    /**
     * 获取模块名称
     *
     * @param channel 管道信息
     * @return 模块名称
     */
    public String getModuleName(Channel channel) {
        String key = channel.remoteAddress().toString();
        return getModuleName(key);
    }

    /**
     * 获取模块名称
     *
     * @param remoteKey 远程唯一标识
     * @return 模块名称
     */
    public String getModuleName(String remoteKey) {
        AppInfo appInfo = appNames.get(remoteKey);
        return appInfo == null ? null : appInfo.getAppName();
    }

    public List<AppInfo> appInfos() {
        return new ArrayList<>(appNames.values());
    }
}
