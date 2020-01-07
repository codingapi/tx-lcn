package com.codingapi.txlcn.protocol.manager.service;


import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.codingapi.txlcn.protocol.Config;
import com.codingapi.txlcn.protocol.PeerEventLoopGroup;
import com.codingapi.txlcn.protocol.manager.Peer;
import com.codingapi.txlcn.protocol.manager.network.message.leader.AnnounceLeader;
import com.codingapi.txlcn.protocol.manager.network.message.leader.Election;
import com.codingapi.txlcn.protocol.manager.network.message.leader.Rejection;
import com.codingapi.txlcn.protocol.message.Connection;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ScheduledFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeadershipService {

  private static final Logger LOGGER = LoggerFactory.getLogger(LeadershipService.class);

  private final ConnectionService connectionService;

  private final Config config;

  private final EventLoopGroup peerEventLoopGroup;

  /**
   * Name of the peer that is announced as leader
   */
  private String leaderName;

  /**
   * Flag that indicates if this peer has started an election currently
   */
  private boolean isElectionPresent;

  /**
   * Timeout {@link Future} of the current election
   */
  private ScheduledFuture electionTimeoutFuture;

  public LeadershipService(ConnectionService connectionService,
      PeerEventLoopGroup peerEventLoopGroup) {
    this(connectionService, peerEventLoopGroup.getConfig(),
        peerEventLoopGroup.getPeerEventLoopGroup());
  }

  private LeadershipService(ConnectionService connectionService, Config config,
      EventLoopGroup peerEventLoopGroup) {
    this.connectionService = connectionService;
    this.config = config;
    this.peerEventLoopGroup = peerEventLoopGroup;
  }

  /**
   * Called when a peer is announced by another peer. Also propagates the leader name to its own
   * neighbourhoods. If announced leader is weaker than this peer, this peer starts an election.
   *
   * @param connection Connection of the peer that announces the leader
   * @param leaderName Name of the peer that is announced as leader
   */
  public void handleLeader(final Connection connection, final String leaderName) {
    final String connectionPeerName = connection.getPeerName();
    if (this.leaderName != null) {
      if (this.leaderName.equals(leaderName)) {
        LOGGER.warn("Ignoring new leader {} from {} since it is already known leader.", leaderName,
            connectionPeerName);
        return;
      }
      final boolean isNewLeaderStronger = !isThisPeerStrongerThan(leaderName);
      LOGGER.warn(
          "A new leader {} from {} is received while there is already a leader {}. Is new leader stronger? {}",
          leaderName, connectionPeerName, this.leaderName, isNewLeaderStronger);
    }

    this.leaderName = leaderName;
    LOGGER.info("Leader from {} is set to {}.", connectionPeerName, leaderName);

    if (isThisPeerStrongerThan(leaderName)) {
      LOGGER.info("Starting a new election since the announced leader {} is weaker.", leaderName);
      startElection();
    }
  }

  /**
   * Called when another peer starts an election. It this peer is stronger than the other peer that
   * starts the election, then this peer rejects the other node and starts a new election itself.
   *
   * @param connection Connection of the peer that starts the election
   */
  public void handleElection(final Connection connection) {
    final String connectionPeerName = connection.getPeerName();

    if (leaderName != null) {
      LOGGER.warn(
          "Election received from {} while there is already a leader {}. Starting new election. ",
          connectionPeerName, leaderName);
    } else {
      LOGGER.info("Election msg of {} is received.", connectionPeerName);
    }

    if (isThisPeerStrongerThan(connectionPeerName)) {
      connection.send(new Rejection());
      LOGGER.info("Rejecting election of {} since it is weaker.", connectionPeerName);
      scheduleElection();
    }
  }

  /**
   * Called when another peer rejects the election started by this peer. Once its election is
   * rejected, this peer waits for some time to check if a leader will be announced or not. If not
   * announced, it starts a new election.
   *
   * @param connection Connection of the peer that rejects the election of this peer
   */
  public void handleRejection(final Connection connection) {
    final String connectionPeerName = connection.getPeerName();
    if (isElectionPresent) {
      if (isThisPeerStrongerThan(connectionPeerName)) {
        LOGGER.warn("Rejection of {} is ignored since it is weaker.", connectionPeerName);
      } else {
        LOGGER.info("{} rejected. Scheduling election timeout.", connectionPeerName);
        if (electionTimeoutFuture != null) {
          electionTimeoutFuture.cancel(false);
          electionTimeoutFuture = null;
        } else {
          LOGGER.warn("Election timeout do not exist!");
        }

        scheduleElectionTimeout(config.getLeaderRejectionTimeoutSeconds());
        isElectionPresent = false;
      }
    } else {
      LOGGER.debug("Rejection of {} is ignored since there is no election", connectionPeerName);
    }
  }

  /**
   * Starts a new election. It does not start the new election immediately. It schedules it to a few
   * millis later to reduce congestion between election of this peer and other peers.
   */
  public void scheduleElection() {
    peerEventLoopGroup
        .schedule((Runnable) this::startElection, Peer.RANDOM.nextInt(100), MILLISECONDS);
  }

  /**
   * Returns the current leader
   *
   * @return current leader
   */
  public String getLeaderName() {
    return leaderName;
  }

  private boolean isThisPeerStrongerThan(final String otherPeerName) {
    return config.getPeerName().compareTo(otherPeerName) > 0;
  }

  private void startElection() {
    if (isElectionPresent) {
      LOGGER.warn("An ongoing election already exists!");
      return;
    }

    isElectionPresent = true;
    leaderName = null;
    final Election election = new Election();
    for (Connection connection : connectionService.getConnections()) {
      if (connection.getPeerName().compareTo(config.getPeerName()) > 0) {
        connection.send(election);
      }
    }

    LOGGER.info("Started an election!");
    scheduleElectionTimeout(config.getLeaderElectionTimeoutSeconds());
  }

  private void scheduleElectionTimeout(final long timeoutSeconds) {
    electionTimeoutFuture = peerEventLoopGroup
        .schedule((Runnable) this::handleElectionTimeout, timeoutSeconds, SECONDS);
  }

  private void handleElectionTimeout() {
    electionTimeoutFuture = null;
    if (isElectionPresent) {
      LOGGER
          .info("Election timed out without getting any rejections. Announcing itself as leader.");
      setThisPeerLeader();
      isElectionPresent = false;
    } else if (leaderName == null) {
      LOGGER.info("Election rejected but there is no leader yet. Starting a new election.");
      scheduleElection();
    } else {
      LOGGER.debug("Election timeout and current leader is already set to {}", leaderName);
    }
  }

  private void setThisPeerLeader() {
    this.leaderName = config.getPeerName();
    LOGGER.info("Announcing itself as leader!");
    final AnnounceLeader announceLeader = new AnnounceLeader(config.getPeerName());
    for (Connection connection : connectionService.getConnections()) {
      connection.send(announceLeader);
    }
  }

}
