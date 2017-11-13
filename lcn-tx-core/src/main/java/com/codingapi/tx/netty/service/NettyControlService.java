package com.codingapi.tx.netty.service;

import io.netty.channel.ChannelHandlerContext; /**
 * create by lorne on 2017/11/11
 */
public interface NettyControlService {
    void restart();


    void executeService(ChannelHandlerContext ctx, String json);


    void uploadModelInfo();

}
