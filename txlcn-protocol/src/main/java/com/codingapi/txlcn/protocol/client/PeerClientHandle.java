package com.codingapi.txlcn.protocol.client;

import com.codingapi.txlcn.protocol.PeerEventLoopGroup;
import com.codingapi.txlcn.protocol.client.service.PeerClientConnectionService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PeerClientHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerClientHandle.class);

    private final EventLoopGroup networkEventLoopGroup;

    private final EventLoopGroup peerEventLoopGroup;

    private final ObjectEncoder encoder;

    private PeerClientConnectionService peerClientConnectionService = new PeerClientConnectionService();

    public PeerClientHandle(PeerEventLoopGroup peerEventLoopGroup){
        this.networkEventLoopGroup = peerEventLoopGroup.getNetworkEventLoopGroup();
        this.peerEventLoopGroup = peerEventLoopGroup.getPeerEventLoopGroup();
        this.encoder = peerEventLoopGroup.getEncoder();
    }

    public void connectTo(String applicationName,String host,int port) {
        final CompletableFuture<Void> futureToNotify = new CompletableFuture<>();
        PeerClient peerClient = new PeerClient(applicationName,host, port);
        connectTo(peerClient,futureToNotify);
    }

    private void connectTo(PeerClient peerClient, final CompletableFuture<Void> futureToNotify) {
        String host = peerClient.getHost();
        int port = peerClient.getPort();
        final PeerClientHandler peerClientHandler = new PeerClientHandler(peerClient,peerClientConnectionService);
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
                            LOGGER.info("Successfully connect to {}:{}, key:{} ", host, port,peerClient.getKey());
                        } else {
                            futureToNotify.completeExceptionally(future.cause());
                            LOGGER.error("Could not connect to " + host + ":" + port, future.cause());
                        }
                    }
            );
        }
    }

    public List<PeerClient> list(){
        return peerClientConnectionService.clients();
    }

}
