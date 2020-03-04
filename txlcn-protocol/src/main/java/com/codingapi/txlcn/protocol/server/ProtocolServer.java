package com.codingapi.txlcn.protocol.server;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.config.Config;
import com.codingapi.txlcn.protocol.handler.ProtocolChannelHandler;
import com.codingapi.txlcn.protocol.handler.ProtocolChannelInitializer;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Slf4j
public class ProtocolServer {

    private final EventLoopGroup acceptorEventLoopGroup;

    private final EventLoopGroup networkEventLoopGroup;

    private final EventExecutorGroup eventExecutorGroup;

    private final Config config;

    private final Protocoler protocoler;


    public ProtocolServer(Config config) {
        this.config = config;

        protocoler = new Protocoler(config);

        acceptorEventLoopGroup = new NioEventLoopGroup(1);
        networkEventLoopGroup = new NioEventLoopGroup(6);
        eventExecutorGroup = new NioEventLoopGroup(10);
    }

    public ChannelFuture start() throws InterruptedException {

        int portToBind = config.getPort();

        ChannelFuture closeFuture = null;

        ProtocolChannelHandler protocolChannelHandler = new ProtocolChannelHandler(protocoler);

        ProtocolChannelInitializer protocolChannelInitializer = new ProtocolChannelInitializer(config,protocolChannelHandler,eventExecutorGroup);


        final ServerBootstrap peerBootstrap = new ServerBootstrap();
        peerBootstrap.group(acceptorEventLoopGroup, networkEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(protocolChannelInitializer);

        final ChannelFuture bindFuture = peerBootstrap.bind(portToBind).sync();

        if (bindFuture.isSuccess()) {
            log.info("Successfully bind to {}",portToBind);
            final Channel serverChannel = bindFuture.channel();

            final SettableFuture<Void> setServerChannelFuture = SettableFuture.create();
            eventExecutorGroup.execute(() -> {
                try {
                    protocoler.setBindChannel(serverChannel);
                    setServerChannelFuture.set(null);
                } catch (Exception e) {
                    setServerChannelFuture.setException(e);
                }
            });

            try {
                setServerChannelFuture.get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("server Couldn't set bind channel to server ", e);
                System.exit(-1);
            }

            closeFuture = serverChannel.closeFuture();
        } else {
            log.error(" server could not bind to " + portToBind, bindFuture.cause());
            System.exit(-1);
        }

        return closeFuture;
    }



    public void connectTo(String host, int port) {
        CompletableFuture<Void> futureToNotify = new CompletableFuture<>();
        connectTo(host, port,futureToNotify);
    }


    public void connectTo(String host, int port, CompletableFuture<Void> futureToNotify) {

        ProtocolChannelHandler protocolChannelHandler = new ProtocolChannelHandler(protocoler);
        ProtocolChannelInitializer protocolChannelInitializer = new ProtocolChannelInitializer(config,protocolChannelHandler,eventExecutorGroup);

        final Bootstrap clientBootstrap = new Bootstrap();
        clientBootstrap.group(networkEventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(protocolChannelInitializer);

        final ChannelFuture connectFuture = clientBootstrap.connect(host, port);
        if (futureToNotify != null) {
            connectFuture.addListener((ChannelFuture future)->{
                        if (future.isSuccess()) {
                            log.info("Successfully connect to {}:{}", host, port);
                            futureToNotify.complete(null);
                        } else {
                            log.error("Could not connect to " + host + ":" + port, future.cause());
                            futureToNotify.completeExceptionally(future.cause());
                        }
                    }
            );
        }
    }



}
