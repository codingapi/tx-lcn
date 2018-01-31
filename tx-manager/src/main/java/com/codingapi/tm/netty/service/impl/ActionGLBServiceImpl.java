package com.codingapi.tm.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tm.manager.service.LoadBalanceService;
import com.codingapi.tm.model.LoadBalanceInfo;
import com.codingapi.tm.netty.service.IActionService;
import com.lorne.core.framework.utils.encode.Base64Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 获取负载模块信息
 * create by lorne on 2017/11/11
 */
@Service(value = "glb")
public class ActionGLBServiceImpl implements IActionService{


    @Autowired
    private LoadBalanceService loadBalanceService;


    @Override
    public String execute(String channelAddress, String key, JSONObject params ) {
        String res;
        String groupId = params.getString("g");
        String k = params.getString("k");

        LoadBalanceInfo loadBalanceInfo =  loadBalanceService.get(groupId,k);
        if(loadBalanceInfo==null){
            res = "";
        }else{
            res = loadBalanceInfo.getData();
        }
        return res;
    }
}
