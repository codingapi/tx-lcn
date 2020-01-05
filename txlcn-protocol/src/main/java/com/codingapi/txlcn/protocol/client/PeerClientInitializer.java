package com.codingapi.txlcn.protocol.client;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.EventExecutorGroup;

public class PeerClientInitializer extends ChannelInitializer<SocketChannel> {

    private final ObjectEncoder encoder;

    private final EventExecutorGroup peerChannelHandlerExecutorGroup;

    private final PeerClientHandler peerClientHandler;

    public PeerClientInitializer(ObjectEncoder encoder, EventExecutorGroup peerChannelHandlerExecutorGroup,
                                 PeerClientHandler peerClientHandler) {
        this.encoder = encoder;
        this.peerChannelHandlerExecutorGroup = peerChannelHandlerExecutorGroup;
        this.peerClientHandler = peerClientHandler;
    }

    @Override
    protected void initChannel(final SocketChannel ch) throws Exception {
        final ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        pipeline.addLast(encoder);
        pipeline.addLast(peerChannelHandlerExecutorGroup, peerClientHandler);
    }

}
