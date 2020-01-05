package com.codingapi.txlcn.protocol.manager.service;


import com.codingapi.txlcn.protocol.Config;
import com.codingapi.txlcn.protocol.manager.Peer;
import com.codingapi.txlcn.protocol.PeerEventLoopGroup;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.manager.network.PeerChannelHandler;
import com.codingapi.txlcn.protocol.manager.network.PeerChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Maintains TCP connections between this peer and its neighbours
 */
public class ConnectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionService.class);

    private final Config config;

    private final EventLoopGroup networkEventLoopGroup;

    private final EventLoopGroup peerEventLoopGroup;

    private final ObjectEncoder encoder;

    // server name -> connection
    private final Map<String, Connection> connections = new HashMap<String, Connection>();


    public ConnectionService(PeerEventLoopGroup peerEventLoopGroup){
        this(peerEventLoopGroup.getConfig(),peerEventLoopGroup.getNetworkEventLoopGroup(),peerEventLoopGroup.getPeerEventLoopGroup(),peerEventLoopGroup.getEncoder());
    }

    private ConnectionService(Config config, EventLoopGroup networkEventLoopGroup, EventLoopGroup peerEventLoopGroup,
                             ObjectEncoder encoder) {
        this.config = config;
        this.networkEventLoopGroup = networkEventLoopGroup;
        this.peerEventLoopGroup = peerEventLoopGroup;
        this.encoder = encoder;
    }

    public void addConnection(final Connection connection) {
        final String peerName = connection.getPeerName();
        final Connection previousConnection = connections.put(peerName, connection);

        LOGGER.info("Connection to " + peerName + " is added.");

        if (previousConnection != null) {
            previousConnection.close();
            LOGGER.warn("Already existing connection to " + peerName + " is closed.");
        }
    }

    public boolean removeConnection(final Connection connection) {
        final boolean removed = connections.remove(connection.getPeerName()) != null;

        if (removed) {
            LOGGER.info(connection + " is removed from connections!");
        } else {
            LOGGER.warn("Connection to " + connection.getPeerName() + " is not removed since not found in connections!");
        }

        return removed;
    }

    public int getNumberOfConnections() {
        return connections.size();
    }

    public boolean isConnectedTo(final String peerName) {
        return connections.containsKey(peerName);
    }

    public Connection getConnection(final String peerName) {
        return connections.get(peerName);
    }

    public Collection<Connection> getConnections() {
        return Collections.unmodifiableCollection(connections.values());
    }

    public void connectTo(final Peer peer, final String host, final int port, final CompletableFuture<Void> futureToNotify) {
        final PeerChannelHandler handler = new PeerChannelHandler(config, peer);
        final PeerChannelInitializer initializer = new PeerChannelInitializer(config, encoder, peerEventLoopGroup, handler);
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
