package com.codingapi.txlcn.protocol.client.network;


import com.codingapi.txlcn.protocol.client.PeerClient;
import com.codingapi.txlcn.protocol.client.network.message.Heartbeat;
import com.codingapi.txlcn.protocol.client.service.PeerClientConnectionService;
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
public class PeerClientHandler extends SimpleChannelInboundHandler<Message> {

  static final String SESSION_ATTRIBUTE_KEY = "session";

  private static final Logger LOGGER = LoggerFactory.getLogger(PeerClientHandler.class);
  private PeerClient peerClient;
  private PeerClientConnectionService peerClientConnectionService;
  public PeerClientHandler(PeerClient peerClient,
      PeerClientConnectionService peerClientConnectionService) {
    this.peerClient = peerClient;
    this.peerClientConnectionService = peerClientConnectionService;
  }

  static Attribute<Connection> getSessionAttribute(ChannelHandlerContext ctx) {
    return ctx.channel().attr(AttributeKey.<Connection>valueOf(SESSION_ATTRIBUTE_KEY));
  }

  @Override
  public void channelActive(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.debug("Channel active {}", ctx.channel().remoteAddress());
    final Connection connection = new Connection(ctx);
    peerClient.bindConnection(connection);
    getSessionAttribute(ctx).set(connection);
    peerClientConnectionService.add(peerClient);
  }

  @Override
  public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
    LOGGER.debug("Channel inactive {}", ctx.channel().remoteAddress());
    final Connection connection = getSessionAttribute(ctx).get();
    peerClientConnectionService.remove(connection);
  }

  @Override
  public void channelRead0(final ChannelHandlerContext ctx, final Message message)
      throws Exception {
    LOGGER.debug("Message {} received from {}", message.getClass(), ctx.channel().remoteAddress());
    final Connection connection = getSessionAttribute(ctx).get();
    message.handle(peerClient, connection);
  }

  @Override
  public void channelReadComplete(final ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    LOGGER.error("Channel failure " + ctx.channel().remoteAddress(), cause);
    ctx.close();
  }


  @Override
  public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) {
    if (evt instanceof IdleStateEvent) {
      final IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
      if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
        final Connection connection = getSessionAttribute(ctx).get();
        connection.send(new Heartbeat());
        LOGGER.debug("send heartbeat to {}", connection.getRemoteAddress());
      }
    }
  }


}
