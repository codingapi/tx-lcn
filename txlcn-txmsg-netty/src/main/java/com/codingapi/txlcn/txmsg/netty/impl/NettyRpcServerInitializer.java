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

import com.codingapi.txlcn.txmsg.RpcServerInitializer;
import com.codingapi.txlcn.txmsg.dto.ManagerProperties;
import com.codingapi.txlcn.txmsg.netty.em.NettyType;
import com.codingapi.txlcn.txmsg.netty.handler.init.NettyRpcServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Service
@Slf4j
public class NettyRpcServerInitializer implements RpcServerInitializer, DisposableBean {

    private final NettyRpcServerChannelInitializer nettyRpcServerChannelInitializer;
    private EventLoopGroup workerGroup;
    private NioEventLoopGroup bossGroup;

    @Autowired
    public NettyRpcServerInitializer(NettyRpcServerChannelInitializer nettyRpcServerChannelInitializer) {
        this.nettyRpcServerChannelInitializer = nettyRpcServerChannelInitializer;
    }


    @Override
    public void init(ManagerProperties managerProperties) {
        NettyContext.type = NettyType.server;
        NettyContext.params = managerProperties;

        nettyRpcServerChannelInitializer.setManagerProperties(managerProperties);

        int port = managerProperties.getRpcPort();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(nettyRpcServerChannelInitializer);

            // Start the server.
            if (StringUtils.hasText(managerProperties.getRpcHost())) {
                b.bind(managerProperties.getRpcHost(), managerProperties.getRpcPort());
            } else {
                b.bind(port);
            }
            log.info("Socket started on {}:{} ",
                    StringUtils.hasText(managerProperties.getRpcHost()) ? managerProperties.getRpcHost() : "0.0.0.0", port);

        } catch (Exception e) {
            // Shut down all event loops to terminate all threads.
            e.printStackTrace();
        }
    }


    @Override
    public void destroy() throws Exception {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }

        log.info("server was down.");
    }


}
