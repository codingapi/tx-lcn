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

import com.codingapi.txlcn.spi.message.listener.ClientInitCallBack;
import com.codingapi.txlcn.spi.message.RpcClientInitializer;
import com.codingapi.txlcn.spi.message.RpcConfig;
import com.codingapi.txlcn.spi.message.dto.TxManagerHost;
import com.codingapi.txlcn.spi.message.netty.bean.SocketManager;
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
import java.util.concurrent.CountDownLatch;

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

    @Autowired
    private RpcConfig rpcConfig;

    @Autowired
    private ClientInitCallBack clientInitCallBack;

    private CountDownLatch latch;

    private EventLoopGroup workerGroup;

    @Override
    public void init(List<TxManagerHost> hosts, CountDownLatch latch) {
        NettyContext.type = NettyType.client;
        NettyContext.params = hosts;
        this.latch = latch;
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
                    log.info("Try connect socket({}) - count {}", socketAddress, i + 1);
                    Bootstrap b = new Bootstrap();
                    b.group(workerGroup);
                    b.channel(NioSocketChannel.class);
                    b.option(ChannelOption.SO_KEEPALIVE, true);
                    b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
                    b.handler(nettyRpcClientHandlerInitHandler);
                    ChannelFuture channelFuture = b.connect(socketAddress).syncUninterruptibly();
                    channelFuture.addListener(future -> latch.countDown());
                    break;
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
            latch.countDown();
            log.info("Already connected socket({}).", socketAddress);
            connected = true;
            break;
        }

        if (!connected) {
            latch.countDown();
            log.warn("Finally, netty connection fail , socket is {}", socketAddress);
            clientInitCallBack.disconnected(socketAddress.toString());
        }
    }

    @Override
    public void destroy() {
        workerGroup.shutdownGracefully();
        log.info("RPC client was down.");
    }
}
