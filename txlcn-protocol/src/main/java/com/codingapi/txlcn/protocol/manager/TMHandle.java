package com.codingapi.txlcn.protocol.manager;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.codingapi.txlcn.protocol.Config;
import com.codingapi.txlcn.protocol.PeerEventLoopGroup;
import com.codingapi.txlcn.protocol.manager.network.TMChannelHandler;
import com.codingapi.txlcn.protocol.manager.network.ChannelInitializer;
import com.codingapi.txlcn.protocol.manager.service.ConnectionService;
import com.codingapi.txlcn.protocol.manager.service.IPingService;
import com.codingapi.txlcn.protocol.manager.service.LeadershipService;
import com.codingapi.txlcn.protocol.message.Connection;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TMHandle {

  private static final Logger LOGGER = LoggerFactory.getLogger(TMHandle.class);

  private final Config config;

  private final int portToBind;

  private final EventLoopGroup acceptorEventLoopGroup;

  private final EventLoopGroup networkEventLoopGroup;

  private final EventLoopGroup peerEventLoopGroup;

  private final ObjectEncoder encoder;

  private final TMPeer peer;

  private Future keepAliveFuture;

  private Future timeoutPingsFuture;

  public TMHandle(Config config, int port, PeerEventLoopGroup peerEventLoopGroupBean,
      ConnectionService connectionService, LeadershipService leadershipService,
      IPingService pingService) {
    this.config = config;
    this.portToBind = port;
    acceptorEventLoopGroup = peerEventLoopGroupBean.getAcceptorEventLoopGroup();
    networkEventLoopGroup = peerEventLoopGroupBean.getNetworkEventLoopGroup();
    peerEventLoopGroup = peerEventLoopGroupBean.getPeerEventLoopGroup();
    encoder = peerEventLoopGroupBean.getEncoder();
    this.peer = new TMPeer(config, connectionService, pingService, leadershipService);
  }


  public String getPeerName() {
    return config.getPeerName();
  }

  public ChannelFuture start() throws InterruptedException {

    ChannelFuture closeFuture = null;

    final TMChannelHandler peerChannelHandler = new TMChannelHandler(config, peer);
    final ChannelInitializer peerChannelInitializer = new ChannelInitializer(config,
        encoder,
        peerEventLoopGroup, peerChannelHandler);
    final ServerBootstrap peerBootstrap = new ServerBootstrap();
    peerBootstrap.group(acceptorEventLoopGroup, networkEventLoopGroup)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .option(ChannelOption.SO_BACKLOG, 100)
        .handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(peerChannelInitializer);

    final ChannelFuture bindFuture = peerBootstrap.bind(portToBind).sync();

    if (bindFuture.isSuccess()) {
      LOGGER.info("{} Successfully bind to {}", config.getPeerName(), portToBind);
      final Channel serverChannel = bindFuture.channel();

      final SettableFuture<Void> setServerChannelFuture = SettableFuture.create();
      peerEventLoopGroup.execute(() -> {
        try {
          peer.setBindChannel(serverChannel);
          setServerChannelFuture.set(null);
        } catch (Exception e) {
          setServerChannelFuture.setException(e);
        }
      });

      try {
        setServerChannelFuture.get(10, TimeUnit.SECONDS);
      } catch (Exception e) {
        LOGGER.error("Couldn't set bind channel to server " + config.getPeerName(), e);
        System.exit(-1);
      }

      final int initialDelay = TMPeer.RANDOM.nextInt(config.getKeepAlivePeriodSeconds());

      this.keepAliveFuture = peerEventLoopGroup
          .scheduleAtFixedRate((Runnable) peer::keepAlivePing, initialDelay,
              config.getKeepAlivePeriodSeconds(), SECONDS);

      this.timeoutPingsFuture = peerEventLoopGroup
          .scheduleAtFixedRate((Runnable) peer::timeoutPings, 0, 100, TimeUnit.MILLISECONDS);

      closeFuture = serverChannel.closeFuture();
    } else {
      LOGGER.error(config.getPeerName() + " could not bind to " + portToBind, bindFuture.cause());
      System.exit(-1);
    }

    return closeFuture;
  }

  public CompletableFuture<Collection<String>> ping() {
    final CompletableFuture<Collection<String>> future = new CompletableFuture<>();
    peerEventLoopGroup.execute(() -> peer.ping(future));
    return future;
  }

  public CompletableFuture<Void> leave() {
    final CompletableFuture<Void> future = new CompletableFuture<>();
    peerEventLoopGroup.execute(() -> peer.leave(future));
    if (keepAliveFuture != null && timeoutPingsFuture != null) {
      keepAliveFuture.cancel(false);
      timeoutPingsFuture.cancel(false);
      keepAliveFuture = null;
      timeoutPingsFuture = null;
    }
    return future;
  }

  public void scheduleLeaderElection() {
    peerEventLoopGroup.execute(peer::scheduleElection);
  }

  public CompletableFuture<Void> connect(final String host, final int port) {
    final CompletableFuture<Void> connectToHostFuture = new CompletableFuture<>();

    peerEventLoopGroup.execute(() -> peer.connectTo(host, port, connectToHostFuture));

    return connectToHostFuture;
  }

  public void disconnect(final String peerName) {
    peerEventLoopGroup.execute(() -> peer.disconnect(peerName));
  }


  public Collection<Connection> connections() {
    return peer.connections();
  }
}
