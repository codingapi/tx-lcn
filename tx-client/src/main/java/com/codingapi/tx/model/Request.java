package com.codingapi.tx.model;

import com.lorne.core.framework.utils.KidUtils;

/**
 * Created by lorne on 2017/6/30.
 */
public class Request {

    /**
     * key
     */
    private String key;
    /**
     * action
     */
    private String action;
    /**
     * params
     */
    private String params;


    public Request(String action, String params) {
        this.action = action;
        this.params = params;
        this.key = KidUtils.generateShortUuid();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String toMsg() {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("a", getAction());
//        jsonObject.put("k", getKey());
//        jsonObject.put("p", getParams());
        String json = "{\"a\":\"" + getAction() + "\",\"k\":\"" + getKey() + "\",\"p\":" + getParams() + "}";
        return json;
    }
}
