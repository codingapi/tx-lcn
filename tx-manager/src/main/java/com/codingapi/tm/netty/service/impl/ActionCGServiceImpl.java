package com.codingapi.tm.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tm.manager.service.TxManagerService;
import com.codingapi.tm.netty.model.TxGroup;
import com.codingapi.tm.netty.service.IActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建事务组
 * create by lorne on 2017/11/11
 */
@Service(value = "cg")
public class ActionCGServiceImpl implements IActionService{


    @Autowired
    private TxManagerService txManagerService;

    @Override
    public String execute(String channelAddress, String key, JSONObject params ) {
        String res = "";
        //获取事务组id
        String groupId = params.getString("g");
        //创建事务组
        TxGroup txGroup = txManagerService.createTransactionGroup(groupId);
        if(txGroup!=null) {
            //设置当前时间到事务对象中。
            txGroup.setNowTime(System.currentTimeMillis());
            res = txGroup.toJsonString(false);
        }else {
            res = "";
        }
        return res;
    }
}
