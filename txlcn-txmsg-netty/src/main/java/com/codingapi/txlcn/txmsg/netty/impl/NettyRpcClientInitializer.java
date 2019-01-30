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

import com.codingapi.txlcn.txmsg.RpcClientInitializer;
import com.codingapi.txlcn.txmsg.RpcConfig;
import com.codingapi.txlcn.txmsg.dto.TxManagerHost;
import com.codingapi.txlcn.txmsg.listener.ClientInitCallBack;
import com.codingapi.txlcn.txmsg.netty.bean.SocketManager;
import com.codingapi.txlcn.txmsg.netty.em.NettyType;
import com.codingapi.txlcn.txmsg.netty.handler.init.NettyRpcClientChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Component
@Slf4j
public class NettyRpcClientInitializer implements RpcClientInitializer, DisposableBean {

    private static NettyRpcClientInitializer INSTANCE;

    private final NettyRpcClientChannelInitializer nettyRpcClientChannelInitializer;

    private final RpcConfig rpcConfig;

    private final ClientInitCallBack clientInitCallBack;

    private EventLoopGroup workerGroup;

    @Autowired
    public NettyRpcClientInitializer(NettyRpcClientChannelInitializer nettyRpcClientChannelInitializer, RpcConfig rpcConfig, ClientInitCallBack clientInitCallBack) {
        this.nettyRpcClientChannelInitializer = nettyRpcClientChannelInitializer;
        this.rpcConfig = rpcConfig;
        this.clientInitCallBack = clientInitCallBack;
        INSTANCE = this;
    }

    public static void reConnect(SocketAddress socketAddress) {
        Objects.requireNonNull(socketAddress, "non support!");
        INSTANCE.connect(socketAddress);
    }

    @Override
    public void init(List<TxManagerHost> hosts, boolean sync) {
        NettyContext.type = NettyType.client;
        NettyContext.params = hosts;
        workerGroup = new NioEventLoopGroup();
        for (TxManagerHost host : hosts) {
            Optional<Future> future = connect(new InetSocketAddress(host.getHost(), host.getPort()));
            if (sync && future.isPresent()) {
                try {
                    future.get().get(10, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }


    @Override
    public synchronized Optional<Future> connect(SocketAddress socketAddress) {
        for (int i = 0; i < rpcConfig.getReconnectCount(); i++) {
            if (SocketManager.getInstance().noConnect(socketAddress)) {
                try {
                    log.info("Try connect socket({}) - count {}", socketAddress, i + 1);
                    Bootstrap b = new Bootstrap();
                    b.group(workerGroup);
                    b.channel(NioSocketChannel.class);
                    b.option(ChannelOption.SO_KEEPALIVE, true);
                    b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
                    b.handler(nettyRpcClientChannelInitializer);
                    return Optional.of(b.connect(socketAddress).syncUninterruptibly());
                } catch (Exception e) {
                    log.warn("Connect socket({}) fail. {}ms latter try again.", socketAddress, rpcConfig.getReconnectDelay());
                    try {
                        Thread.sleep(rpcConfig.getReconnectDelay());
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    continue;
                }
            }
            // 忽略已连接的连接
            return Optional.empty();
        }

        log.warn("Finally, netty connection fail , socket is {}", socketAddress);
        clientInitCallBack.connectFail(socketAddress.toString());
        return Optional.empty();
    }

    @Override
    public void destroy() {
        workerGroup.shutdownGracefully();
        log.info("RPC client was down.");
    }
}
