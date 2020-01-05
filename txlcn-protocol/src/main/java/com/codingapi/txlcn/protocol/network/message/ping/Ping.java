package com.codingapi.txlcn.protocol.network.message.ping;

import com.codingapi.txlcn.protocol.IPeer;
import com.codingapi.txlcn.protocol.Peer;
import com.codingapi.txlcn.protocol.network.Connection;
import com.codingapi.txlcn.protocol.network.message.Message;

/**
 * Contains information about a Ping operation initiated by a peer
 */
public class Ping implements Message {

    private static final long serialVersionUID = -4362142418375530711L;

    private final String peerName;

    private final int ttl;

    private final int hops;

    private final long pingTimeoutDurationInMillis;

    private transient long pingStartTimestamp;

    public Ping(String peerName, int ttl, int hops, long pingTimeoutDurationInMillis) {
        this.peerName = peerName;
        this.ttl = ttl;
        this.hops = hops;
        this.pingTimeoutDurationInMillis = pingTimeoutDurationInMillis;
    }

    public String getPeerName() {
        return peerName;
    }

    public int getTtl() {
        return ttl;
    }

    public int getHops() {
        return hops;
    }

    public long getPingTimeoutDurationInMillis() {
        return pingTimeoutDurationInMillis;
    }

    public long getPingStartTimestamp() {
        return pingStartTimestamp;
    }

    public void setPingStartTimestamp(long pingStartTimestamp) {
        this.pingStartTimestamp = pingStartTimestamp;
    }

    public Ping next() {
        return ttl > 1 ? new Ping(peerName, ttl - 1, hops + 1, pingTimeoutDurationInMillis) : null;
    }

    @Override
    public void handle(IPeer peer, Connection connection) {
        peer.optional(Peer.class).ifPresent(p->p.handlePing(connection, this));
    }

    @Override
    public String toString() {
        return "Ping{" +
                "peerName=" + peerName +
                ", ttl=" + ttl +
                ", hops=" + hops +
                ", pingStartTimestamp=" + pingStartTimestamp +
                '}';
    }

}
