package com.codingapi.txlcn.protocol.client;

import com.codingapi.txlcn.protocol.PeerEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PeerClientHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerClientHandle.class);

    private final EventLoopGroup networkEventLoopGroup;

    private final EventLoopGroup peerEventLoopGroup;

    private final ObjectEncoder encoder;

    private final Map<String, PeerClient> clients = new HashMap<String, PeerClient>();

    public PeerClientHandle(PeerEventLoopGroup peerEventLoopGroup){
        this.networkEventLoopGroup = peerEventLoopGroup.getNetworkEventLoopGroup();
        this.peerEventLoopGroup = peerEventLoopGroup.getPeerEventLoopGroup();
        this.encoder = peerEventLoopGroup.getEncoder();
    }


    public void connectTo(PeerClient peerClient,String peerName) {
        final CompletableFuture<Void> futureToNotify = new CompletableFuture<>();
        connectTo(peerClient,peerName,futureToNotify);
    }

    public void connectTo(PeerClient peerClient,String peerName, final CompletableFuture<Void> futureToNotify) {
        String host = peerClient.getHost();
        int port = peerClient.getPort();
        clients.put(peerName,peerClient);
        final PeerClientHandler peerClientHandler = new PeerClientHandler(peerClient);
        final PeerClientInitializer initializer = new PeerClientInitializer(encoder,peerEventLoopGroup,peerClientHandler);
        final Bootstrap clientBootstrap = new Bootstrap();
        clientBootstrap.group(networkEventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(initializer);

        final ChannelFuture connectFuture = clientBootstrap.connect(host, port);
        if (futureToNotify != null) {
            connectFuture.addListener((ChannelFuture future)->{
                        if (future.isSuccess()) {
                            futureToNotify.complete(null);
                            LOGGER.info("Successfully connect to {}:{}", host, port);
                        } else {
                            futureToNotify.completeExceptionally(future.cause());
                            LOGGER.error("Could not connect to " + host + ":" + port, future.cause());
                        }
                    }
            );
        }
    }



}
