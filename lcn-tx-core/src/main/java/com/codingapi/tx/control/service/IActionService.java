package com.codingapi.tx.control.service;

import com.alibaba.fastjson.JSONObject;

/**
 * create by lorne on 2017/11/13
 */
public interface IActionService {

    String execute(JSONObject resObj, String json);

}
