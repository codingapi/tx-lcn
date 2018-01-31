package com.codingapi.tm.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tm.config.ConfigReader;
import com.codingapi.tm.manager.service.TxManagerService;
import com.codingapi.tm.netty.service.IActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 心跳包
 * create by lorne on 2017/11/11
 */
@Service(value = "h")
public class ActionHServiceImpl implements IActionService{


    @Autowired
    private ConfigReader configReader;

    @Override
    public String execute(String channelAddress, String key, JSONObject params ) {
        return String.valueOf(configReader.getTransactionNettyDelayTime());
    }
}
