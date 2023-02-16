package com.codingapi.tm.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tm.manager.service.TxManagerService;
import com.codingapi.tm.netty.service.IActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 关闭事务组
 * create by lorne on 2017/11/11
 */
@Service(value = "ctg")
public class ActionCTGServiceImpl implements IActionService{


    @Autowired
    private TxManagerService txManagerService;

    @Override
    public String execute(String channelAddress, String key, JSONObject params ) {
        //获取事务组
        String groupId = params.getString("g");
        //获取参与者执行结果
        int state = params.getInteger("s");
        String res = String.valueOf(txManagerService.closeTransactionGroup(groupId,state));
        return res;
    }
}
