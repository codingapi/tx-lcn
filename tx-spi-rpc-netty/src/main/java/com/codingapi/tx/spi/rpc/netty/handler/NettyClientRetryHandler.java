package com.codingapi.tx.spi.rpc.netty.handler;

import com.codingapi.tx.spi.rpc.MessageConstants;
import com.codingapi.tx.commons.util.RandomUtils;
import com.codingapi.tx.spi.rpc.ClientInitCallBack;
import com.codingapi.tx.spi.rpc.dto.MessageDto;
import com.codingapi.tx.spi.rpc.netty.SocketManager;
import com.codingapi.tx.spi.rpc.netty.bean.NettyRpcCmd;
import com.codingapi.tx.spi.rpc.netty.impl.NettyContext;
import com.codingapi.tx.spi.rpc.netty.impl.NettyRpcClientInitializer;
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
        messageDto.setAction(MessageConstants.ACTION_RPC_HEART);
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
