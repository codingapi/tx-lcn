package com.codingapi.tx.spi.rpc.netty.handler;

import com.codingapi.tx.spi.rpc.RpcAnswer;
import com.codingapi.tx.spi.rpc.dto.RpcCmd;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@ChannelHandler.Sharable
@Slf4j
@Component
public class RpcAnswerHandler extends SimpleChannelInboundHandler<RpcCmd> {

    @Autowired
    private RpcAnswer rpcClientAnswer;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcCmd cmd) {
        String remoteKey = ctx.channel().remoteAddress().toString();
        cmd.setRemoteKey(remoteKey);
        rpcClientAnswer.callback(cmd);
    }
}
