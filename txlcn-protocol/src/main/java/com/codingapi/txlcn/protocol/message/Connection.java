package com.codingapi.txlcn.protocol.message;


import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Maintains a TCP connection between the local peer and a neighbour
 */
public class Connection {

    private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);

    private final InetSocketAddress remoteAddress;

    private ChannelHandlerContext ctx;

    private String peerName;

    public Connection(ChannelHandlerContext ctx) {
        this.remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        this.ctx = ctx;
    }

    public InetSocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    public String getPeerName() {
        return peerName;
    }

    public void setPeerName(final String peerName) {
        if (this.peerName == null) {
            this.peerName = peerName;
        } else {
            LOGGER.warn("peer name {} set again for connection {}", peerName, this);
        }
    }

    public void send(final Message msg) {
        if (ctx != null) {
            ctx.writeAndFlush(msg);
        } else {
            LOGGER.error("Can not send message " + msg.getClass() + " to " + toString());
        }
    }

    public void close() {
        LOGGER.debug("Closing session of {}", toString());
        if (ctx != null) {
            ctx.close();
            ctx = null;
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;

        Connection that = (Connection) other;

        return !(peerName != null ? !peerName.equals(that.peerName) : that.peerName != null);
    }

    @Override
    public int hashCode() {
        return peerName != null ? peerName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "remoteAddress=" + remoteAddress +
                ", isOpen=" + (ctx != null) +
                ", peerName='" + peerName + '\'' +
                '}';
    }
}
