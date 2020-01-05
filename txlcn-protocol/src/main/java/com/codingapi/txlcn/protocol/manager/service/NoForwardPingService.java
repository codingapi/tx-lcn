package com.codingapi.txlcn.protocol.manager.service;


import com.codingapi.txlcn.protocol.Config;
import com.codingapi.txlcn.protocol.PeerEventLoopGroup;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.manager.network.message.KeepAlive;
import com.codingapi.txlcn.protocol.manager.network.message.ping.CancelPings;
import com.codingapi.txlcn.protocol.manager.network.message.ping.CancelPongs;
import com.codingapi.txlcn.protocol.manager.network.message.ping.Ping;
import com.codingapi.txlcn.protocol.manager.network.message.ping.Pong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;

/**
 * Maintains all of the ongoing Ping operations either initiated by this peer or other peers in the network
 */
public class NoForwardPingService implements IPingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoForwardPingService.class);

    private final ConnectionService connectionService;

    private final LeadershipService leadershipService;

    private final Config config;

    private final Map<String, PingContext> currentPings = new HashMap<String, PingContext>();

    private int autoPingCount;

    public NoForwardPingService(ConnectionService connectionService, LeadershipService leadershipService, PeerEventLoopGroup peerEventLoopGroup) {
        this.connectionService = connectionService;
        this.leadershipService = leadershipService;
        this.config = peerEventLoopGroup.getConfig();
    }

    /**
     * Sends a keep-alive message to its neighbours to notify its presence. It sends {@link Ping} messages to others
     * instead of keep-alive messages at frequency given with {@link Config#autoDiscoveryPingFrequency}
     *
     * @param discoveryPingEnabled if true, then peer is allowed to trigger a discovery ping to learn about other peers
     */
    @Override
    public void keepAlive(final boolean discoveryPingEnabled) {
        if (!currentPings.containsKey(config.getPeerName())) {
            if (incrementAutoPingCountAndCheckIfFullPing() && discoveryPingEnabled) {
                if (!currentPings.containsKey(config.getPeerName())) {
                    discoveryPing();
                } else {
                    LOGGER.info("Periodic discovery ping skipped since there exists a ping already");
                }
            } else {
                keepAlive();
            }
        }
    }

    /**
     * Initiates a Ping operation in the network. If there is an ongoing Ping operation, it only attaches to it without
     * starting a new Ping.
     *
     * @param future future to be informed once the Ping is completed.
     */
    @Override
    public void ping(final CompletableFuture<Collection<String>> future) {
        PingContext pingContext = currentPings.get(config.getPeerName());
        if (pingContext == null) {
            pingContext = discoveryPing();
        } else {
            LOGGER.info("Attaching to the already existing ping context");
        }

        if (future != null) {
            pingContext.addFuture(future);
        }
    }

    /**
     * Handles a Ping operation initiated by another node. If the received {@link Ping} message is allowed to be
     * propagated,it sends a {@link Pong} message back to the neighbour that has sent the {@link Ping} message to this peer.
     *
     * @param bindAddress Network address that this peer bind
     * @param connection  Connection of the neighbour that sent the Ping message
     * @param ping        Ping message that is received. It can be initiated by the neighbour with the given connection or
     *                    any other peer that has no direct connection to this peer
     */
    @Override
    public void handlePing(final InetSocketAddress bindAddress, final Connection connection, final Ping ping) {
        final String pingPeerName = ping.getPeerName();
        if (currentPings.containsKey(pingPeerName)) {
            LOGGER.info("Skipping ping of {} since it is already handled.", pingPeerName);
            return;
        }

        if (pingPeerName.equals(connection.getPeerName())) {
            LOGGER.info("Handling {} of initiator {} with ttl={}", ping, pingPeerName, ping.getTtl());
        } else {
            LOGGER.info("Handling {} of initiator {} and forwarder {} with ttl={} and hops={}", ping, pingPeerName,
                    connection.getPeerName(), ping.getTtl(), ping.getHops());
        }

        ping.setPingStartTimestamp(System.currentTimeMillis());
        currentPings.put(pingPeerName, new PingContext(ping, connection));

        final Pong pong = new Pong(pingPeerName, config.getPeerName(), config.getPeerName(),
                bindAddress.getAddress().getHostAddress(), bindAddress.getPort(), ping.getHops() + 1, 0);
        connection.send(pong);
    }

    /**
     * Propagates the existing Ping operations to a new connection of this peer
     *
     * @param connection new connection that existing Ping operations will be propagated
     */
    @Override
    public void propagatePingsToNewConnection(final Connection connection) {
        for (PingContext pingContext : currentPings.values()) {
            if (!pingContext.getPeerName().equals(connection.getPeerName())) {
                final Ping next = pingContext.getPing().next();
                if (next != null) {
                    connection.send(next);
                    LOGGER.info("{} sent to new connection {}", next, connection.getPeerName());
                }
            }
        }
    }

    /**
     * Handles a {@link Pong} message that is sent in response to a {@link Ping} message propagated by this peer.
     *
     * @param pong Pong message that is sent in response to a {@link Ping} message propagated by this peer.
     */
    @Override
    public void handlePong(final Pong pong) {
        if (pong.getPeerName().equals(config.getPeerName())) {
            LOGGER.warn("Received {} from itself", pong);
            return;
        }
        final String pingPeerName = pong.getPingPeerName();
        final PingContext pingContext = currentPings.get(pingPeerName);
        if (pingContext == null) {
            LOGGER.warn("No ping context is found for {} from {} for initiator {}", pong, pong.getPeerName(),
                    pingPeerName);
            return;
        }

        pingContext.handlePong(config.getPeerName(), pong);
    }

    /**
     * Removes {@link Pong} messages sent by the disconnected peer to cancel out its presence in ongoing Ping
     * operations.
     *
     * @param disconnectedPeerName name of the disconnected peer
     */
    @Override
    public void cancelPongs(final String disconnectedPeerName) {
        final Iterator<Entry<String, PingContext>> pingIt = currentPings.entrySet().iterator();

        final CancelPongs cancelPongs = new CancelPongs(disconnectedPeerName);
        while (pingIt.hasNext()) {
            final Entry<String, PingContext> pingEntry = pingIt.next();
            final String pingPeerName = pingEntry.getKey();
            final PingContext pingContext = pingEntry.getValue();

            // Remove Pong messages of disconnected peer in ongoing ping operations
            if (pingContext.removePong(disconnectedPeerName)) {
                final Connection pingOwnerConnection = pingContext.getConnection();
                if (pingOwnerConnection != null) {
                    LOGGER.info("Removed pong of {} in ping of {}. Forwarding {} to {}", disconnectedPeerName, pingPeerName,
                            cancelPongs, pingOwnerConnection.getPeerName());
                    pingOwnerConnection.send(cancelPongs);
                } else {
                    LOGGER.info("Removed pong of {} in ping of {}", disconnectedPeerName, pingPeerName);
                }
            }

            // Check if a ping message needs to be propagated to the neighbours again for an ongoing ping.
            // Ping operation is re-propagated if any Pong message of any other peer is sent to this peer via
            // disconnected peer. If there exists such a peer, called orphan peer, Ping message is re-propagated
            // to check if there is still an active connection between this peer and the orphan peer
            boolean rePing = false;
            for (Pong pong : new ArrayList<>(pingContext.getPongs())) {
                if (pong.getSenderPeerName().equals(disconnectedPeerName)) {
                    pingContext.removePong(pong.getPeerName());
                    rePing = true;
                    LOGGER.info("Removed Pong of {} in ping of {} since it was sent by {}", pong.getPeerName(), pingPeerName,
                            disconnectedPeerName);
                    final Connection connection = pingContext.getConnection();
                    if (connection != null) {
                        final CancelPongs msg = new CancelPongs(pong.getPeerName());
                        connection.send(msg);
                        LOGGER.info("Forwarded {} to {} for ping of {}", msg, connection.getPeerName(), pingPeerName);
                    }
                }
            }

            if (rePing) {
                final Ping next = pingContext.getPing().next();
                if (next != null) {
                    LOGGER.info("Will re-send {}", next);
                    for (Connection connection : connectionService.getConnections()) {
                        if (!(connection.getPeerName().equals(pingPeerName) || connection.getPeerName()
                                .equals(disconnectedPeerName) || !connection.equals(pingContext.getConnection()))) {
                            connection.send(next);
                            LOGGER.info("{} re-sent to {} because {} left", next, connection.getPeerName(), disconnectedPeerName);
                        }
                    }
                }
            }
        }
    }

    /**
     * Cancels out Ping operations of the disconnected peer. Additionally, neighbours that sent {@link Pong} messages
     * to the cancelled Ping are also notified.
     *
     * @param connection           connection of the neighbour that notifies this peer about disconnected peer
     * @param disconnectedPeerName name of disconnected peer
     */
    @Override
    public void cancelPings(final Connection connection, final String disconnectedPeerName) {
        final Iterator<Entry<String, PingContext>> pingIt = currentPings.entrySet().iterator();

        while (pingIt.hasNext()) {
            final Entry<String, PingContext> pingEntry = pingIt.next();
            final String pingPeerName = pingEntry.getKey();
            final PingContext pingContext = pingEntry.getValue();

            final Connection pingOwnerConnection = pingContext.getConnection();
            boolean shouldRemove = pingPeerName.equals(disconnectedPeerName) && connection.equals(pingOwnerConnection);
            if (!shouldRemove) {
                shouldRemove = pingOwnerConnection != null && pingOwnerConnection.getPeerName().equals(disconnectedPeerName);
            }

            if (shouldRemove) {
                LOGGER.info("Removing ping of {} since it is disconnected", pingPeerName);

                pingIt.remove();
                final CancelPings cancelPings = new CancelPings(disconnectedPeerName);
                for (Pong pong : pingContext.getPongs()) {
                    final Connection c = connectionService.getConnection(pong.getPeerName());
                    if (c != null) {
                        c.send(cancelPings);
                        LOGGER.info("{} sent to {}", cancelPings, pong.getPeerName());
                    } else {
                        LOGGER.warn("{} not sent to {} since there is no connection", cancelPings, pong.getPeerName());
                    }
                }
            }
        }
    }

    /**
     * Cancels this peers ping operation and notifies its futures
     */
    @Override
    public void cancelOwnPing() {
        final PingContext pingContext = currentPings.get(config.getPeerName());
        if (pingContext != null) {
            LOGGER.info("Cancelling own ping");
            for (CompletableFuture<Collection<String>> future : pingContext.getFutures()) {
                future.cancel(true);
            }
        }
    }

    /**
     * Terminates ongoing Ping operations that completed the timeout duration. If there is a terminated Ping operation
     * initiated by this peer, {@link Pong} messages collected are returned.
     *
     * @return Pong messages return in response to the ongoing Ping operation of this peer.
     */
    @Override
    public Collection<Pong> timeoutPings() {
        Collection<Pong> pongs = Collections.emptyList();

        final Iterator<Entry<String, PingContext>> pingIt = currentPings.entrySet().iterator();

        while (pingIt.hasNext()) {
            final Entry<String, PingContext> pingEntry = pingIt.next();
            final String pingPeerName = pingEntry.getKey();
            final PingContext pingContext = pingEntry.getValue();

            if (pingContext.isTimeout()) {
                pingIt.remove();

                if (config.getPeerName().equals(pingPeerName)) {
                    pongs = pingContext.getPongs();
                    final Set<String> peers = new HashSet<>();
                    for (Pong pong : pongs) {
                        peers.add(pong.getPeerName());
                    }
                    peers.add(config.getPeerName());
                    LOGGER.info("Ping for {} has timed out. Notifying futures with # peers: {}", pingContext.getPeerName(),
                            peers.size());
                    for (CompletableFuture<Collection<String>> future : pingContext.getFutures()) {
                        future.complete(peers);
                    }
                } else {
                    LOGGER.info("Ping for {} has timed out.", pingContext.getPeerName());
                }
            }
        }

        return pongs;
    }

    private PingContext discoveryPing() {
        final int ttl = config.getPingTTL();

        LOGGER.info("Doing a full ping with ttl={}", ttl);

        final Ping ping = new Ping(config.getPeerName(), ttl, 0, config.getPingTimeoutMillis());
        ping.setPingStartTimestamp(System.currentTimeMillis());
        final PingContext pingContext = new PingContext(ping, null);
        currentPings.put(config.getPeerName(), pingContext);

        for (Connection connection : connectionService.getConnections()) {
            connection.send(ping);
        }

        return pingContext;
    }

    private boolean incrementAutoPingCountAndCheckIfFullPing() {
        return ++autoPingCount % config.getAutoDiscoveryPingFrequency() == 0;
    }

    private void keepAlive() {
        LOGGER.debug("Doing a keep-alive ping");

        final KeepAlive keepAlive = new KeepAlive();
        for (Connection connection : connectionService.getConnections()) {
            connection.send(keepAlive);
        }
    }

}
