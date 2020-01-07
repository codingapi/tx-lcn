package com.codingapi.txlcn.protocol.manager.network;


import com.codingapi.txlcn.protocol.Config;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;

public class PeerChannelInitializer extends ChannelInitializer<SocketChannel> {

  private final Config config;

  private final ObjectEncoder encoder;

  private final EventExecutorGroup peerChannelHandlerExecutorGroup;

  private final PeerChannelHandler peerChannelHandler;

  public PeerChannelInitializer(Config config, ObjectEncoder encoder,
      EventExecutorGroup peerChannelHandlerExecutorGroup,
      PeerChannelHandler peerChannelHandler) {
    this.config = config;
    this.encoder = encoder;
    this.peerChannelHandlerExecutorGroup = peerChannelHandlerExecutorGroup;
    this.peerChannelHandler = peerChannelHandler;
  }

  @Override
  protected void initChannel(final SocketChannel ch) throws Exception {
    final ChannelPipeline pipeline = ch.pipeline();

    pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
    pipeline.addLast(encoder);
    pipeline.addLast(new IdleStateHandler(config.getMaxReadIdleSeconds(), 0, 0));

    pipeline.addLast(peerChannelHandlerExecutorGroup, peerChannelHandler);
  }

}
