package com.codingapi.tx.control.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.control.service.IActionService;
import com.codingapi.tx.control.service.TransactionControlService;
import com.codingapi.tx.framework.utils.SocketUtils;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class TransactionControlServiceImpl implements TransactionControlService{

    private Logger logger = LoggerFactory.getLogger(TransactionControlServiceImpl.class);


    @Autowired
    private ApplicationContext spring;



    @Override
    public void notifyTransactionMsg(ChannelHandlerContext ctx,JSONObject resObj, String json) {


        String action = resObj.getString("a");
        String key = resObj.getString("k");

        IActionService actionService = spring.getBean(action, IActionService.class);

        String res = actionService.execute(resObj, json);


        JSONObject data = new JSONObject();
        data.put("k", key);
        data.put("a", action);

        JSONObject params = new JSONObject();
        params.put("d", res);
        data.put("p", params);

        SocketUtils.sendMsg(ctx, data.toString());

        logger.info("send notify data ->" + data.toString());
    }
}
