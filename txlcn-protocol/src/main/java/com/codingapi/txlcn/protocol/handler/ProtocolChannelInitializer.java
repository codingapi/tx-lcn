package com.codingapi.txlcn.protocol.handler;

import com.codingapi.txlcn.protocol.config.Config;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
public class ProtocolChannelInitializer extends ChannelInitializer<SocketChannel> {

    private Config config;

    private ProtocolChannelHandler protocolChannelHandler;

    private EventExecutorGroup eventExecutorGroup;

    public ProtocolChannelInitializer(Config config,
                                      ProtocolChannelHandler protocolChannelHandler,
                                      EventExecutorGroup eventExecutorGroup) {
        this.config = config;
        this.protocolChannelHandler = protocolChannelHandler;
        this.eventExecutorGroup = eventExecutorGroup;
    }

    @Override
    protected void initChannel(final SocketChannel ch) throws Exception {
        final ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new LengthFieldPrepender(4, false));
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast(new IdleStateHandler(config.getMaxReadIdleSeconds(), config.getMaxWriteIdleSeconds(), 0));

        pipeline.addLast(eventExecutorGroup, protocolChannelHandler);
    }
}
