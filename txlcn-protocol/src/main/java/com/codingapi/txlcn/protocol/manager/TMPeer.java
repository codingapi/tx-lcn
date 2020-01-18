package com.codingapi.txlcn.protocol.manager;


import static java.lang.Math.min;

import com.codingapi.txlcn.protocol.Config;
import com.codingapi.txlcn.protocol.manager.network.message.ping.CancelPongs;
import com.codingapi.txlcn.protocol.manager.network.message.ping.Ping;
import com.codingapi.txlcn.protocol.manager.network.message.ping.Pong;
import com.codingapi.txlcn.protocol.manager.service.ConnectionService;
import com.codingapi.txlcn.protocol.manager.service.IPingService;
import com.codingapi.txlcn.protocol.manager.service.LeadershipService;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;
import io.netty.channel.Channel;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TMPeer {

  public static final Random RANDOM = new Random();
  private static final Logger LOGGER = LoggerFactory.getLogger(TMPeer.class);
  private final Config config;

  private final ConnectionService connectionService;

  private final IPingService pingService;

  private final LeadershipService leadershipService;

  private Channel bindChannel;

  private boolean running = true;

  public TMPeer(Config config, ConnectionService connectionService, IPingService pingService,
      LeadershipService leadershipService) {
    this.config = config;
    this.connectionService = connectionService;
    this.pingService = pingService;
    this.leadershipService = leadershipService;
  }

  public void handleConnectionOpened(Connection connection, String leaderName) {
    if (isShutdown()) {
      LOGGER.warn("New connection of {} ignored since not running", connection.getPeerName());
      return;
    }

    if (connection.getPeerName().equals(config.getPeerName())) {
      LOGGER.error("Can not connect to itself. Closing new connection.");
      connection.close();
      return;
    }

    connectionService.addConnection(connection);
    if (leaderName != null) {
      final String currentLeaderName = leadershipService.getLeaderName();
      if (currentLeaderName == null) {
        leadershipService.handleLeader(connection, leaderName);
      } else if (!leaderName.equals(currentLeaderName)) {
        LOGGER
            .info("Known leader {} and leader {} announced by {} are different.", currentLeaderName,
                leaderName, connection);
        leadershipService.scheduleElection();
      }
    }
    pingService.propagatePingsToNewConnection(connection);
  }

  public void handleConnectionClosed(Connection connection) {
    if (connection == null) {
      return;
    }

    final String connectionPeerName = connection.getPeerName();
    if (connectionPeerName == null || connectionPeerName.equals(config.getPeerName())) {
      return;
    }

    if (connectionService.removeConnection(connection)) {
      cancelPings(connection, connectionPeerName);
      cancelPongs(connectionPeerName);
    }

    if (connectionPeerName.equals(leadershipService.getLeaderName())) {
      LOGGER.warn("Starting an election since connection to current leader {} is closed.",
          connectionPeerName);
      leadershipService.scheduleElection();
    }
  }

  public void cancelPings(final Connection connection, final String removedPeerName) {
    if (running) {
      pingService.cancelPings(connection, removedPeerName);
    } else {
      LOGGER.warn("Pings of {} can't be cancelled since not running", removedPeerName);
    }
  }

  public void handlePing(Connection connection, Ping ping) {
    if (running) {
      pingService.handlePing((InetSocketAddress) bindChannel.localAddress(), connection, ping);
    } else {
      LOGGER.warn("Ping of {} is ignored since not running", connection.getPeerName());
    }
  }

  public void handlePong(Connection connection, Pong pong) {
    if (running) {
      pingService.handlePong(pong);
    } else {
      LOGGER.warn("Pong of {} is ignored since not running", connection.getPeerName());
    }
  }

  public void keepAlivePing() {
    if (isShutdown()) {
      LOGGER.warn("Periodic ping ignored since not running");
      return;
    }

    final int numberOfConnections = connectionService.getNumberOfConnections();
    if (numberOfConnections > 0) {
      final boolean discoveryPingEnabled =
          numberOfConnections < config.getMinNumberOfActiveConnections();
      pingService.keepAlive(discoveryPingEnabled);
    } else {
      LOGGER.debug("No auto ping since there is no connection");
    }
  }

  public void timeoutPings() {
    if (isShutdown()) {
      LOGGER.warn("Timeout pings ignored since not running");
      return;
    }

    final Collection<Pong> pongs = pingService.timeoutPings();
    final int availableConnectionSlots =
        config.getMinNumberOfActiveConnections() - connectionService.getNumberOfConnections();

    if (availableConnectionSlots > 0) {
      List<Pong> notConnectedPeers = new ArrayList<>();
      for (Pong pong : pongs) {
        if (!config.getPeerName().equals(pong.getPeerName()) && !connectionService
            .isConnectedTo(pong.getPeerName())) {
          notConnectedPeers.add(pong);
        }
      }

      Collections.shuffle(notConnectedPeers);
      for (int i = 0, j = min(availableConnectionSlots, notConnectedPeers.size()); i < j; i++) {
        final Pong peerToConnect = notConnectedPeers.get(i);
        final String host = peerToConnect.getServerHost();
        final int port = peerToConnect.getServerPort();
        LOGGER.info("Auto-connecting to {} via {}:{}", peerToConnect.getPeerName(),
            peerToConnect.getPeerName(), host,
            port);
        connectTo(host, port, null);
      }
    }
  }

  public void cancelPongs(final String removedPeerName) {
    if (isShutdown()) {
      LOGGER.warn("Pongs of {} not cancelled since not running", removedPeerName);
      return;
    }

    pingService.cancelPongs(removedPeerName);
  }

  public void handleLeader(final Connection connection, String leaderName) {
    if (isShutdown()) {
      LOGGER.warn("Leader announcement of {} from connection {} ignored since not running",
          leaderName, connection.getPeerName());
      return;
    }

    leadershipService.handleLeader(connection, leaderName);
  }

  public void handleElection(final Connection connection) {
    if (isShutdown()) {
      LOGGER.warn("Election of {} ignored since not running", connection.getPeerName());
      return;
    }

    leadershipService.handleElection(connection);
  }

  public void handleRejection(final Connection connection) {
    if (isShutdown()) {
      LOGGER.warn("Rejection of {} ignored since not running", connection.getPeerName());
      return;

    }

    leadershipService.handleRejection(connection);
  }

  public void scheduleElection() {
    if (isShutdown()) {
      LOGGER.warn("Election not scheduled since not running");
      return;
    }

    leadershipService.scheduleElection();
  }

  public void broadcastMsg(Message message) {
    Collection<Connection> connections = connectionService.getConnections();
    if (connections != null) {
      for (Connection connection : connections) {
        connection.send(message);
      }
    }
  }


  public void sendMsg(String peerName, Message message) {
    Connection connection = connectionService.getConnection(peerName);
    if (connection != null) {
      connection.send(message);
    } else {
      LOGGER.warn("This peer {} is not connected to {}", config.getPeerName(), peerName);
    }

  }

  public void disconnect(final String peerName) {
    if (isShutdown()) {
      LOGGER.warn("Not disconnected from {} since not running", peerName);
      return;
    }

    final Connection connection = connectionService.getConnection(peerName);
    if (connection != null) {
      LOGGER.info("Disconnecting this peer {} from {}", config.getPeerName(), peerName);
      connection.close();
    } else {
      LOGGER.warn("This peer {} is not connected to {}", config.getPeerName(), peerName);
    }
  }

  public String getLeaderName() {
    return leadershipService.getLeaderName();
  }

  public void setBindChannel(final Channel bindChannel) {
    this.bindChannel = bindChannel;
  }


  public void ping(final CompletableFuture<Collection<String>> futureToNotify) {
    if (isShutdown()) {
      futureToNotify.completeExceptionally(new RuntimeException("Disconnected!"));
      return;
    }

    pingService.ping(futureToNotify);
  }

  public void leave(final CompletableFuture<Void> futureToNotify) {
    if (isShutdown()) {
      LOGGER.warn("{} already shut down!", config.getPeerName());
      futureToNotify.complete(null);
      return;
    }

    bindChannel.closeFuture().addListener(future -> {
      if (future.isSuccess()) {
        futureToNotify.complete(null);
      } else {
        futureToNotify.completeExceptionally(future.cause());
      }
    });

    pingService.cancelOwnPing();
    pingService.cancelPongs(config.getPeerName());
    final CancelPongs cancelPongs = new CancelPongs(config.getPeerName());
    for (Connection connection : connectionService.getConnections()) {
      connection.send(cancelPongs);
      connection.close();
    }
    bindChannel.close();
    running = false;
  }

  public void connectTo(final String host, final int port,
      final CompletableFuture<Void> futureToNotify) {
    if (running) {
      connectionService.connectTo(this, host, port, futureToNotify);
    } else {
      futureToNotify.completeExceptionally(new RuntimeException("Server is not running"));
    }
  }

  public Collection<Connection> connections() {
    return connectionService.getConnections();
  }

  private boolean isShutdown() {
    return !running;
  }


}
