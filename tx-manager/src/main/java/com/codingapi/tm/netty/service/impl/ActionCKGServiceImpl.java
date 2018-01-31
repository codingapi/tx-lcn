package com.codingapi.tm.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tm.manager.service.TxManagerService;
import com.codingapi.tm.netty.service.IActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 检查事务组
 * create by lorne on 2017/11/11
 */
@Service(value = "ckg")
public class ActionCKGServiceImpl implements IActionService{


    @Autowired
    private TxManagerService txManagerService;

    @Override
    public String execute(String channelAddress, String key, JSONObject params ) {
        String res = "";
        String groupId = params.getString("g");
        String taskId = params.getString("t");
        int bs = txManagerService.cleanNotifyTransaction(groupId,taskId);

        res = String.valueOf(bs);
        return res;
    }
}
