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
package com.codingapi.txlcn.spi.message.netty;


import com.codingapi.txlcn.spi.message.netty.bean.NettyRpcCmd;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.dto.RpcCmd;
import com.codingapi.txlcn.spi.message.dto.RpcResponseState;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by lorne on 2017/6/30.
 */
@Slf4j
public class SocketManager {

    private Map<String, String> appNames;

    private ScheduledExecutorService executorService;

    private ChannelGroup channels;

    private static SocketManager manager = null;

    private int attrDelayTime;


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
        try {
            executorService.schedule(() -> {
                String key = channel.toString();
                appNames.remove(key);
            }, attrDelayTime, TimeUnit.SECONDS);
        } catch (RejectedExecutionException ignored) {
            // down server.
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

    public MessageDto request(String key, RpcCmd cmd) throws RpcException {
        NettyRpcCmd nettyRpcCmd = (NettyRpcCmd) cmd;
        log.debug("get channel, key:{}", key);
        Channel channel = getChannel(key);
        log.debug("write and flush sync");
        channel.writeAndFlush(nettyRpcCmd);
        log.debug("await response");
        nettyRpcCmd.await();
        MessageDto res = cmd.loadResult();
        nettyRpcCmd.loadRpcContent().clear();
        return res;
    }


    public List<String> loadAllRemoteKey() {
        List<String> allKeys = new ArrayList<>();
        for (Channel channel : channels) {
            allKeys.add(channel.remoteAddress().toString());
        }
        return allKeys;
    }

    public void setAttrDelayTime(int attrDelayTime) {
        this.attrDelayTime = attrDelayTime;
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

    public List<String> moduleList(String moduleName) {
        List<String> allKeys = new ArrayList<>();
        for (Channel channel : channels) {
            if (getModuleName(channel).equals(moduleName)) {
                allKeys.add(channel.remoteAddress().toString());
            }
        }
        return allKeys;
    }


    public void bindModuleName(String remoteKey, String moduleName) {
        appNames.put(remoteKey, moduleName);
    }

    public String getModuleName(Channel channel) {
        String key = channel.remoteAddress().toString();
        return getModuleName(key);
    }

    public String getModuleName(String remoteKey) {
        return appNames.get(remoteKey);
    }

}
