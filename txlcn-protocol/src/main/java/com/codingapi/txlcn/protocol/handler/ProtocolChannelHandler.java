package com.codingapi.txlcn.protocol.handler;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.config.Config;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Heartbeat;
import com.codingapi.txlcn.protocol.message.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Slf4j
@ChannelHandler.Sharable
public class ProtocolChannelHandler  extends SimpleChannelInboundHandler<Message> {

    private final Protocoler protocoler;

    private static final String SESSION_ATTRIBUTE_KEY = "session";

    @Getter
    private final ApplicationContext applicationContext;

    /**
     * 异步处理消息，避免堵塞netty线程影响消息通讯.
     */
    private Executor executors;

    private Config config;

    public ProtocolChannelHandler(Protocoler protocoler, ApplicationContext applicationContext, Config config) {
        this.protocoler = protocoler;
        this.applicationContext = applicationContext;
        this.config = config;
        this.executors =  Executors.newFixedThreadPool(config.getHandleThreads());
    }

    static Attribute<Connection> getSessionAttribute(ChannelHandlerContext ctx) {
        return ctx.channel().attr(AttributeKey.<Connection>valueOf(SESSION_ATTRIBUTE_KEY));
    }


    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel active {}", ctx.channel().remoteAddress());
        final Connection connection = new Connection(ctx,config);
        getSessionAttribute(ctx).set(connection);
        protocoler.handleConnectionOpened(connection);
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel inactive {}", ctx.channel().remoteAddress());
        final Connection connection = getSessionAttribute(ctx).get();
        protocoler.handleConnectionClosed(connection);
    }


    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final Message message) throws Exception {
        log.debug("Message {} received from {}", message.getClass(), ctx.channel().remoteAddress());
        final Connection connection = getSessionAttribute(ctx).get();
        executors.execute(()->{
            try {
                message.handle(applicationContext,protocoler, connection);
            } catch (Exception e) {
                log.error("message handle fail.",e);
            }
        });
    }


    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) {
        if (evt instanceof IdleStateEvent) {
            final IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                log.warn("Channel idle {}", ctx.channel().remoteAddress());
                ctx.close();
            }

            if(idleStateEvent.state() == IdleState.WRITER_IDLE){
//                log.debug("send heart message to {} ", ctx.channel().remoteAddress());
                ctx.writeAndFlush(new Heartbeat());
            }
        }
    }

}
