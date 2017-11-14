package com.codingapi.tx.control.service;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;

/**
 * create by lorne on 2017/11/11
 */
public interface TransactionControlService {


    void notifyTransactionMsg(ChannelHandlerContext ctx, JSONObject resObj, String json);
}
