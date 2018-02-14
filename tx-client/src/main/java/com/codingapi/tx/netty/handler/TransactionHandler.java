package com.codingapi.tx.netty.handler;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.framework.utils.SocketManager;
import com.codingapi.tx.framework.utils.SocketUtils;
import com.codingapi.tx.netty.service.NettyControlService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;

/**
 * Created by lorne on 2017/6/30.
 */
@ChannelHandler.Sharable
public class TransactionHandler extends ChannelInboundHandlerAdapter {


    private Logger logger = LoggerFactory.getLogger(TransactionHandler.class);


    private NettyControlService nettyControlService;

    private String heartJson;

    private Executor threadPool;


    public TransactionHandler(Executor threadPool,NettyControlService nettyControlService, int delay) {
        this.threadPool = threadPool;
        this.nettyControlService = nettyControlService;

        SocketManager.getInstance().setDelay(delay);

        //心跳包
        JSONObject heartJo = new JSONObject();
        heartJo.put("a", "h");
        heartJo.put("k", "h");
        heartJo.put("p", "{}");
        heartJson = heartJo.toString();

    }


    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {

        final String json = SocketUtils.getJson(msg);

        logger.debug("TxManager-response->" + json);

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                nettyControlService.executeService(ctx, json);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        logger.info("disconnection  -->" + ctx);

        SocketManager.getInstance().setNetState(false);
        //链接断开,重新连接
        nettyControlService.restart();
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        SocketManager.getInstance().setCtx(ctx);

        logger.info("connection -->" + ctx);

        //通道激活后进行心跳检查
        SocketUtils.sendMsg(ctx, heartJson);

        nettyControlService.uploadModelInfo();
    }


    /**
     * 当客户端的所有ChannelHandler中4s内没有write事件，则会触发userEventTriggered方法
     *
     * @param ctx  管道
     * @param evt  状态
     * @throws Exception 异常数据
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //心跳配置
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                //表示已经多久没有收到数据了
                //ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                //表示已经多久没有发送数据了
                SocketUtils.sendMsg(ctx, heartJson);
                logger.debug("hart data --->" + heartJson);
            } else if (event.state() == IdleState.ALL_IDLE) {
                //表示已经多久既没有收到也没有发送数据了
            }
        }
    }


}
