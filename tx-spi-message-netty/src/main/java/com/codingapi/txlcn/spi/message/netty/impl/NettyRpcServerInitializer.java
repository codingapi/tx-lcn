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

import com.codingapi.txlcn.spi.message.RpcServerInitializer;
import com.codingapi.txlcn.spi.message.dto.ManagerProperties;
import com.codingapi.txlcn.spi.message.netty.em.NettyType;
import com.codingapi.txlcn.spi.message.netty.handler.NettyRpcServerHandlerInitHandler;
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

    @Autowired
    private NettyRpcServerHandlerInitHandler nettyRpcServerHandlerInitHandler;

    private EventLoopGroup workerGroup;
    private NioEventLoopGroup bossGroup;


    @Override
    public void init(ManagerProperties managerProperties) {
        NettyContext.type = NettyType.server;
        NettyContext.params = managerProperties;

        nettyRpcServerHandlerInitHandler.setManagerProperties(managerProperties);

        int port = managerProperties.getRpcPort();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(nettyRpcServerHandlerInitHandler);

            // Start the server.
            b.bind(port);
            log.info("Socket started on port(s):{}(socket)",port);

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
