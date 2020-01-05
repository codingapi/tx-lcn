package com.codingapi.txlcn.protocol.manager.service;


import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.manager.network.message.ping.Ping;
import com.codingapi.txlcn.protocol.manager.network.message.ping.Pong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static java.util.Collections.unmodifiableList;

/**
 * Maintains all information related to an ongoing Ping operation.
 */
public class PingContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingContext.class);

    private final Ping ping;

    private final Connection connection;

    // peer name -> pong
    private final Map<String, Pong> pongs = new HashMap<>();

    private final List<CompletableFuture<Collection<String>>> futures = new ArrayList<>();

    public PingContext(Ping ping, Connection connection) {
        this.ping = ping;
        this.connection = connection;
    }

    public String getPeerName() {
        return ping.getPeerName();
    }

    public Ping getPing() {
        return ping;
    }

    public Connection getConnection() {
        return connection;
    }

    public Collection<Pong> getPongs() {
        return Collections.unmodifiableCollection(pongs.values());
    }

    public boolean handlePong(final String thisServerName, final Pong pong) {
        final String pongServerName = pong.getPeerName();
        if (pongs.containsKey(pongServerName)) {
            LOGGER.debug("{} from {} is already handled for {}", pong, pongServerName, ping.getPeerName());
            return false;
        }

        pongs.put(pongServerName, pong);

        LOGGER.debug("Handling {} from {} for {}. Pong #: {}", pong, pongServerName, ping.getPeerName(), pongs.size());

        if (!thisServerName.equals(ping.getPeerName())) {
            if (connection != null) {
                final Pong next = pong.next(thisServerName);
                if (next != null) {
                    LOGGER.debug("Forwarding {} to {} for initiator {}", pong, connection.getPeerName(), ping.getPeerName());
                    connection.send(next);
                } else {
                    LOGGER.error("Invalid {} received from {} for {}", pong, pongServerName, ping.getPeerName());
                }
            } else {
                LOGGER.error("No connection is found in ping context for {} from {} for {}", pong, pongServerName,
                        ping.getPeerName());
            }
        }

        return true;
    }

    public void addFuture(CompletableFuture<Collection<String>> future) {
        futures.add(future);
    }

    public boolean isTimeout() {
        return ping.getPingStartTimestamp() + ping.getPingTimeoutDurationInMillis() <= System.currentTimeMillis();
    }

    public List<CompletableFuture<Collection<String>>> getFutures() {
        return unmodifiableList(futures);
    }

    public boolean removePong(final String serverName) {
        return pongs.remove(serverName) != null;
    }

    @Override
    public String toString() {
        return "PingContext{" +
                "pongs=" + pongs +
                ", connection=" + connection +
                ", ping=" + ping +
                '}';
    }

}
