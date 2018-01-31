package com.codingapi.tm.netty.service;

import com.alibaba.fastjson.JSONObject;

/**
 * create by lorne on 2017/11/11
 */
public interface IActionService {


    String execute(String channelAddress,String key,JSONObject params);

}
