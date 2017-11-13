package com.codingapi.tx.control.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.control.service.IActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 通知补偿
 * create by lorne on 2017/11/13
 */
@Service(value = "c")
public class ActionCServiceImpl implements IActionService {


    private Logger logger = LoggerFactory.getLogger(ActionCServiceImpl.class);

    @Override
    public String execute(JSONObject resObj, String json) {

        System.out.println(resObj);

        return "1";
    }


}
