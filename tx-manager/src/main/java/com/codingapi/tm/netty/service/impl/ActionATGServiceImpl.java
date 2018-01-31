package com.codingapi.tm.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tm.manager.service.TxManagerService;
import com.codingapi.tm.netty.model.TxGroup;
import com.codingapi.tm.netty.service.IActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 添加事务组
 * create by lorne on 2017/11/11
 */
@Service(value = "atg")
public class ActionATGServiceImpl implements IActionService{


    @Autowired
    private TxManagerService txManagerService;

    @Override
    public String execute(String channelAddress,String key,JSONObject params ) {
        String res = "";
        String groupId = params.getString("g");
        String taskId = params.getString("t");
        String methodStr = params.getString("ms");
        int isGroup = params.getInteger("s");

        TxGroup txGroup = txManagerService.addTransactionGroup(groupId, taskId, isGroup, channelAddress, methodStr);

        if(txGroup!=null) {
            txGroup.setNowTime(System.currentTimeMillis());
            res = txGroup.toJsonString(false);
        }else {
             res = "";
        }
        return res;
    }
}
