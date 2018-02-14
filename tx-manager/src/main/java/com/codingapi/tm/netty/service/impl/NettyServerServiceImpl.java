package com.codingapi.tm.netty.service.impl;

import com.codingapi.tm.Constants;
import com.codingapi.tm.config.ConfigReader;
import com.codingapi.tm.netty.handler.TxCoreServerHandler;
import com.codingapi.tm.netty.service.NettyServerService;
import com.codingapi.tm.netty.service.NettyService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by lorne on 2017/6/30.
 */
@Service
public class NettyServerServiceImpl implements NettyServerService,DisposableBean {


    @Autowired
    private NettyService nettyService;

    private Logger logger = LoggerFactory.getLogger(NettyServerServiceImpl.class);

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private TxCoreServerHandler txCoreServerHandler;

    private ExecutorService threadPool = Executors.newFixedThreadPool(100);

    @Autowired
    private ConfigReader configReader;


    @Override
    public void start() {
        final int heartTime = configReader.getTransactionNettyHeartTime()+10;
        txCoreServerHandler = new TxCoreServerHandler(threadPool,nettyService);
        bossGroup = new NioEventLoopGroup(50); // (1)
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("timeout", new IdleStateHandler(heartTime, heartTime, heartTime, TimeUnit.SECONDS));

                            ch.pipeline().addLast(new LengthFieldPrepender(4, false));
                            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));

                            ch.pipeline().addLast(txCoreServerHandler);
                        }
                    });

            // Start the server.
            b.bind(Constants.socketPort);
            logger.info("Socket started on port(s): " + Constants.socketPort + " (socket)");

        } catch (Exception e) {
            // Shut down all event loops to terminate all threads.
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }

    }

    @Override
    public void destroy() throws Exception {
        close();
        threadPool.shutdown();
    }
}
