package com.codingapi.tx.spi.rpc.netty.handler;

import com.codingapi.tx.spi.rpc.netty.bean.NettyRpcCmd;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@ChannelHandler.Sharable
@Slf4j
public class RpcCmdEncoder extends MessageToMessageEncoder<NettyRpcCmd> {


    @Override
    protected void encode(ChannelHandlerContext ctx, NettyRpcCmd cmd, List<Object> out) throws Exception {
        log.debug("send->{}", cmd);
        out.add(cmd);
    }
}
