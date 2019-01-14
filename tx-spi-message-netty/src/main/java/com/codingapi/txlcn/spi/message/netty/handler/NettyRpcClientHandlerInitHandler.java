/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.spi.message.netty.handler;

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
