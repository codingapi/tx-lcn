package com.codingapi.tm.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tm.framework.utils.SocketManager;
import com.codingapi.tm.manager.ModelInfoManager;
import com.codingapi.tm.model.ModelInfo;
import com.codingapi.tm.netty.service.IActionService;
import org.springframework.stereotype.Service;

/**
 * 上传模块信息
 * create by lorne on 2017/11/11
 */
@Service(value = "umi")
public class ActionUMIServiceImpl implements IActionService {


    @Override
    public String execute(String channelAddress, String key, JSONObject params) {
        String res = "1";

        String uniqueKey = params.getString("u");
        String ipAddress = params.getString("i");
        String model = params.getString("m");


        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setChannelName(channelAddress);
        modelInfo.setIpAddress(ipAddress);
        modelInfo.setModel(model);
        modelInfo.setUniqueKey(uniqueKey);

        ModelInfoManager.getInstance().addModelInfo(modelInfo);

        SocketManager.getInstance().onLine(channelAddress, uniqueKey);

        return res;
    }
}
