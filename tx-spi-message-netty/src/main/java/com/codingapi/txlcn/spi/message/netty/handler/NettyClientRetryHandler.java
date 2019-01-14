package com.codingapi.txlcn.spi.message.netty.handler;

import com.codingapi.txlcn.spi.message.MessageConstants;
import com.codingapi.txlcn.commons.util.RandomUtils;
import com.codingapi.txlcn.spi.message.ClientInitCallBack;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.netty.SocketManager;
import com.codingapi.txlcn.spi.message.netty.bean.NettyRpcCmd;
import com.codingapi.txlcn.spi.message.netty.impl.NettyContext;
import com.codingapi.txlcn.spi.message.netty.impl.NettyRpcClientInitializer;
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

    @Autowired
    private NettyRpcClientInitializer nettyRpcClientInitializer;

    @Autowired
    private ClientInitCallBack clientInitCallBack;

    private int keepSize;

    private NettyRpcCmd heartCmd;

    public NettyClientRetryHandler() {
        MessageDto messageDto = new MessageDto();
        messageDto.setAction(MessageConstants.ACTION_HEART_CHECK);
        heartCmd = new NettyRpcCmd();
        heartCmd.setMsg(messageDto);
        heartCmd.setKey(RandomUtils.randomKey());
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
        log.error("keepSize:{},nowSize:{}",keepSize,SocketManager.getInstance().currentSize());

        SocketAddress socketAddress =  ctx.channel().remoteAddress();
        log.error("socketAddress:{} ", socketAddress);
        nettyRpcClientInitializer.connect(socketAddress);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("NettyClientRetryHandler - exception . ",cause);

        if(cause instanceof ConnectException){
            int size = SocketManager.getInstance().currentSize();
            Thread.sleep(1000*15);
            log.error("current size:{}  ",size);
            log.error("try connect tx-manager:{} ",ctx.channel().remoteAddress());
            nettyRpcClientInitializer.connect(ctx.channel().remoteAddress());
        }
        //发送数据包检测是否断开连接.
        ctx.writeAndFlush(heartCmd);

    }
}
