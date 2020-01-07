package com.codingapi.txlcn.protocol.manager.network;


import com.codingapi.txlcn.protocol.Config;
import com.codingapi.txlcn.protocol.manager.Peer;
import com.codingapi.txlcn.protocol.manager.network.message.Handshake;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
public class PeerChannelHandler extends SimpleChannelInboundHandler<Message> {

  static final String SESSION_ATTRIBUTE_KEY = "session";

  private static final Logger LOGGER = LoggerFactory.getLogger(PeerChannelHandler.class);
  private final Config config;
  private final Peer peer;

  public PeerChannelHandler(Config config, Peer peer) {
    this.config = config;
    this.peer = peer;
  }

  static Attribute<Connection> getSessionAttribute(ChannelHandlerContext ctx) {
    return ctx.channel().attr(AttributeKey.<Connection>valueOf(SESSION_ATTRIBUTE_KEY));
  }

  @Override
  public void channelActive(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.debug("Channel active {}", ctx.channel().remoteAddress());
    final Connection connection = new Connection(ctx);
    getSessionAttribute(ctx).set(connection);
    ctx.writeAndFlush(new Handshake(config.getPeerName(), peer.getLeaderName()));
  }

  @Override
  public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.debug("Channel inactive {}", ctx.channel().remoteAddress());
    final Connection connection = getSessionAttribute(ctx).get();
    peer.handleConnectionClosed(connection);
  }

  @Override
  public void channelRead0(final ChannelHandlerContext ctx, final Message message)
      throws Exception {
    LOGGER.debug("Message {} received from {}", message.getClass(), ctx.channel().remoteAddress());
    final Connection connection = getSessionAttribute(ctx).get();
    message.handle(peer, connection);
  }

  @Override
  public void channelReadComplete(final ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    LOGGER.error("Channel failure " + ctx.channel().remoteAddress(), cause);
    ctx.close();
    peer.handleConnectionClosed(getSessionAttribute(ctx).get());
  }

  @Override
  public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) {
    if (evt instanceof IdleStateEvent) {
      final IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
      if (idleStateEvent.state() == IdleState.READER_IDLE) {
        LOGGER.warn("Channel idle {}", ctx.channel().remoteAddress());
        ctx.close();
      }
    }
  }

}
