package com.lorne.tx.mq.service.impl;

import com.lorne.tx.Constants;
import com.lorne.tx.compensate.service.CompensateService;
import com.lorne.tx.mq.handler.TransactionHandler;
import com.lorne.tx.mq.model.Request;
import com.lorne.tx.mq.service.NettyDistributeService;
import com.lorne.tx.mq.service.NettyService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Created by lorne on 2017/6/30.
 */
@Service
public class NettyServiceImpl implements NettyService {


    @Autowired
    private NettyDistributeService nettyDistributeService;

    @Autowired
    private CompensateService compensateService;

    private TransactionHandler transactionHandler;

    private EventLoopGroup workerGroup;


    private static volatile boolean isStarting = false;


    private Logger logger = LoggerFactory.getLogger(NettyServiceImpl.class);

    @Override
    public synchronized void start() {
        if (isStarting) {
            return;
        }
        isStarting = true;
        nettyDistributeService.loadTxServer();

        String host = Constants.txServer.getHost();
        int port = Constants.txServer.getPort();
        final int heart = Constants.txServer.getHeart();
        int delay = Constants.txServer.getDelay();

        transactionHandler = new TransactionHandler(this, delay);
        workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {

                    ch.pipeline().addLast("timeout", new IdleStateHandler(heart, heart, heart, TimeUnit.SECONDS));

                    ch.pipeline().addLast(new LengthFieldPrepender(4, false));
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

                    ch.pipeline().addLast(transactionHandler);
                }
            });
            // Start the client.
            logger.info("连接manager-socket服务-> host:" + host + ",port:" + port);
            ChannelFuture future = b.connect(host, port); // (5)

            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (!channelFuture.isSuccess()) {
                        channelFuture.channel().eventLoop().schedule(new Runnable() {
                            @Override
                            public void run() {
                                isStarting = false;
                                start();
                            }
                        }, 5, TimeUnit.SECONDS);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

//            isStarting = false;
//
//            //断开重新连接机制
//            close();
//
//            if (e instanceof ConnectTimeoutException) {
//                start();
//            }
        }
    }

    @Override
    public synchronized void close() {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
            workerGroup = null;
            TransactionHandler.net_state = false;
            isStarting = false;
        }
    }

    @Override
    public synchronized void restart() {
        close();
        start();
    }

    @Override
    public String sendMsg(Request request) {
        return transactionHandler.sendMsg(request);
    }

    @Override
    public boolean checkState() {
        if (!TransactionHandler.net_state) {
            logger.error("socket服务尚未建立连接成功,将在此等待2秒.");
            try {
                Thread.sleep(1000 * 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!TransactionHandler.net_state) {
                logger.error("socket还未连接成功,请检查TxManager服务后再试.");
                return false;
            }
        }

        return true;
    }

    @Override
    public long checkCompensate(String taskId) {
        return compensateService.countCompensateByTaskId(taskId);
    }

    @Override
    public void executeCompensate(String taskId) {
        compensateService.executeCompensateByTaskId(taskId);
    }
}
