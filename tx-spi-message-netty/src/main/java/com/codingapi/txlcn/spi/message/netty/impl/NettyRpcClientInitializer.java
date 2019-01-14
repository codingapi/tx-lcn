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

import com.codingapi.txlcn.spi.message.RpcClientInitializer;
import com.codingapi.txlcn.spi.message.RpcConfig;
import com.codingapi.txlcn.spi.message.dto.TxManagerHost;
import com.codingapi.txlcn.spi.message.netty.SocketManager;
import com.codingapi.txlcn.spi.message.netty.em.NettyType;
import com.codingapi.txlcn.spi.message.netty.handler.NettyRpcClientHandlerInitHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
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
public class NettyRpcClientInitializer implements RpcClientInitializer, DisposableBean {


    @Autowired
    private NettyRpcClientHandlerInitHandler nettyRpcClientHandlerInitHandler;

    private EventLoopGroup workerGroup;

    @Autowired
    private RpcConfig rpcConfig;


    @Override
    public void init(List<TxManagerHost> hosts) {
        NettyContext.type = NettyType.client;
        NettyContext.params = hosts;
        workerGroup = new NioEventLoopGroup();
        for (TxManagerHost host : hosts) {
            connect(new InetSocketAddress(host.getHost(), host.getPort()));
        }
    }


    @Override
    public synchronized void connect(SocketAddress socketAddress) {
        boolean connected = false;
        for (int i = 0; i < rpcConfig.getReconnectCount(); i++) {
            if (SocketManager.getInstance().noConnect(socketAddress)) {
                try {
                    log.warn("current manager size:{}", SocketManager.getInstance().currentSize());

                    log.info("try reconnect {} - {}", socketAddress, i + 1);
                    Bootstrap b = new Bootstrap();
                    b.group(workerGroup);
                    b.channel(NioSocketChannel.class);
                    b.option(ChannelOption.SO_KEEPALIVE, true);
                    b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
                    b.handler(nettyRpcClientHandlerInitHandler);
                    ChannelFuture channelFuture = b.connect(socketAddress).syncUninterruptibly();
                    log.info("client -> {} , state:{}", socketAddress, channelFuture.isSuccess());
                    connected = true;
                    break;

                } catch (Exception e) {
                    log.warn("reconnect fail. will latter try again.");
                    try {
                        Thread.sleep(1000 * rpcConfig.getReconnectDelay());
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        if (!connected) {
            log.warn("finally, netty connection fail , address is {}", socketAddress);
        }
    }


    @Override
    public void destroy() throws Exception {
        workerGroup.shutdownGracefully();
        log.info("client was down.");
    }
}
