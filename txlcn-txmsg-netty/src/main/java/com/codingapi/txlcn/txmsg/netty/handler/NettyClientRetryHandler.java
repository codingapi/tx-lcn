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
package com.codingapi.txlcn.txmsg.netty.handler;

import com.codingapi.txlcn.txmsg.MessageConstants;
import com.codingapi.txlcn.common.util.id.RandomUtils;
import com.codingapi.txlcn.txmsg.listener.ClientInitCallBack;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.netty.bean.SocketManager;
import com.codingapi.txlcn.txmsg.netty.bean.NettyRpcCmd;
import com.codingapi.txlcn.txmsg.netty.impl.NettyContext;
import com.codingapi.txlcn.txmsg.netty.impl.NettyRpcClientInitializer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.net.SocketAddress;
import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/21
 *
 * @author codingapi
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class NettyClientRetryHandler extends ChannelInboundHandlerAdapter {
    private final ClientInitCallBack clientInitCallBack;

    private int keepSize;

    private NettyRpcCmd heartCmd;

    @Autowired
    public NettyClientRetryHandler(ClientInitCallBack clientInitCallBack) {
        MessageDto messageDto = new MessageDto();
        messageDto.setAction(MessageConstants.ACTION_HEART_CHECK);
        heartCmd = new NettyRpcCmd();
        heartCmd.setMsg(messageDto);
        heartCmd.setKey(RandomUtils.simpleKey());
        this.clientInitCallBack = clientInitCallBack;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        keepSize = NettyContext.currentParam(List.class).size();

        clientInitCallBack.connected(ctx.channel().remoteAddress().toString());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.error("keepSize:{},nowSize:{}", keepSize, SocketManager.getInstance().currentSize());

        SocketAddress socketAddress = ctx.channel().remoteAddress();
        log.error("socketAddress:{} ", socketAddress);
        NettyRpcClientInitializer.reConnect(socketAddress);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("NettyClientRetryHandler - exception . ", cause);

        if (cause instanceof ConnectException) {
            int size = SocketManager.getInstance().currentSize();
            Thread.sleep(1000 * 15);
            log.error("current size:{}  ", size);
            log.error("try connect tx-manager:{} ", ctx.channel().remoteAddress());
            NettyRpcClientInitializer.reConnect(ctx.channel().remoteAddress());
        }
        //发送数据包检测是否断开连接.
        ctx.writeAndFlush(heartCmd);

    }
}
