package com.codingapi.txlcn.protocol.message;


import io.netty.channel.ChannelHandlerContext;
import java.net.InetSocketAddress;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maintains a TCP connection between the local peer and a neighbour
 */
public class Connection {

  private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);

  private final InetSocketAddress remoteAddress;

  private ChannelHandlerContext ctx;

  /**
   * peer name is an connection label.
   */
  private String peerName;

  @Getter
  private final String remoteHost;

  @Getter
  private final int remotePort;

  public String uniqueKey(){
    return String.format("%s:%d",remoteHost,remotePort);
  }

  public Connection(ChannelHandlerContext ctx) {
    this.remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
    this.ctx = ctx;
    this.remoteHost = remoteAddress.getAddress().getHostAddress();
    this.remotePort = remoteAddress.getPort();
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
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }

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
