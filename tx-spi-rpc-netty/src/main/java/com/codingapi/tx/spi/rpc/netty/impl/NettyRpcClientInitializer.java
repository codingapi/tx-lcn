package com.codingapi.tx.spi.rpc.netty.impl;

import com.codingapi.tx.spi.rpc.RpcClientInitializer;
import com.codingapi.tx.spi.rpc.dto.TxManagerHost;
import com.codingapi.tx.spi.rpc.netty.SocketManager;
import com.codingapi.tx.spi.rpc.netty.em.NettyType;
import com.codingapi.tx.spi.rpc.netty.handler.NettyRpcClientHandlerInitHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author ujued
 */
@Service
@Slf4j
public class NettyRpcClientInitializer implements RpcClientInitializer, DisposableBean {


    @Autowired
    private NettyRpcClientHandlerInitHandler nettyRpcClientHandlerInitHandler;

    private EventLoopGroup workerGroup;


    @Override
    public void init(List<TxManagerHost> hosts) {
        NettyContext.type = NettyType.clent;
        NettyContext.params = hosts;
        workerGroup = new NioEventLoopGroup();
        for(TxManagerHost host:hosts){
            connect(new InetSocketAddress(host.getHost(),host.getPort()));
        }
    }


    @Override
    public synchronized void connect(SocketAddress socketAddress){
        if(SocketManager.getInstance().noConnect(socketAddress)) {
            try {
                log.info(" try connect {} ",socketAddress);
                Bootstrap b = new Bootstrap();
                b.group(workerGroup);
                b.channel(NioSocketChannel.class);
                b.option(ChannelOption.SO_KEEPALIVE, true);
                b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
                b.handler(nettyRpcClientHandlerInitHandler);
                ChannelFuture channelFuture = b.connect(socketAddress).syncUninterruptibly();
                log.info("client -> {} , state:{}", socketAddress, channelFuture.isSuccess());
            } catch (Exception e) {

                if (e instanceof ConnectException) {
                    try {
                        Thread.sleep(1000 * 15);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    log.warn("current manager size:{}", SocketManager.getInstance().currentSize());
                    connect(socketAddress);
                }
            }
        }
    }


    @Override
    public void destroy() throws Exception {
        workerGroup.shutdownGracefully();
        log.info("client was down.");
    }
}
