package com.codingapi.tx.client.springcloud.spi.message.netty.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Component
public class NettyRpcClientHandlerInitHandler extends ChannelInitializer<Channel> {

    @Autowired
    private RpcAnswerHandler rpcAnswerHandler;

    @Autowired
    private NettyClientRetryHandler nettyClientRetryHandler;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new LengthFieldPrepender(4, false));
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

        ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        ch.pipeline().addLast(new ObjectEncoder());

        ch.pipeline().addLast(new RpcCmdDecoder());
        ch.pipeline().addLast(new RpcCmdEncoder());
        ch.pipeline().addLast(nettyClientRetryHandler);
        ch.pipeline().addLast(new SocketManagerInitHandler());
        ch.pipeline().addLast(rpcAnswerHandler);
    }
}
