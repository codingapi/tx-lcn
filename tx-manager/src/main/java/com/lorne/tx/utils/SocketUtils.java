package com.lorne.tx.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by lorne on 2017/7/6.
 */
public class SocketUtils {

    public static String getJson(Object msg) {
        String json;
        try {
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            json = new String(bytes);
        } finally {
            ReferenceCountUtil.release(msg);
        }
        return json;

    }

    public static void sendMsg(ChannelHandlerContext ctx, String msg){
        ctx.writeAndFlush(Unpooled.buffer().writeBytes(msg.getBytes()));
    }


    public static void sendMsg(Channel ctx,String msg){
        ctx.writeAndFlush(Unpooled.buffer().writeBytes(msg.getBytes()));
    }
}
