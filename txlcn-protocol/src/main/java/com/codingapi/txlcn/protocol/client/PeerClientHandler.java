package com.codingapi.txlcn.protocol.client;


import com.codingapi.txlcn.protocol.network.Connection;
import com.codingapi.txlcn.protocol.network.message.Message;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Sharable
public class PeerClientHandler extends SimpleChannelInboundHandler<Message> {

    static final String SESSION_ATTRIBUTE_KEY = "session";

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerClientHandler.class);

    static Attribute<Connection> getSessionAttribute(ChannelHandlerContext ctx) {
        return ctx.channel().attr(AttributeKey.<Connection>valueOf(SESSION_ATTRIBUTE_KEY));
    }

    private PeerClient peerClient;

    public PeerClientHandler(PeerClient peerClient) {
        this.peerClient = peerClient;
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("Channel active {}", ctx.channel().remoteAddress());
        final Connection connection = new Connection(ctx);
        getSessionAttribute(ctx).set(connection);
    }

    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("Channel inactive {}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final Message message) throws Exception {
        LOGGER.debug("Message {} received from {}", message.getClass(), ctx.channel().remoteAddress());
        final Connection connection = getSessionAttribute(ctx).get();
        message.handle(peerClient,connection);
    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        LOGGER.error("Channel failure " + ctx.channel().remoteAddress(), cause);
        ctx.close();
    }


}
