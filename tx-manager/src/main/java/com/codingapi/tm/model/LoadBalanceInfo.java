package com.codingapi.tm.model;

/**
 * 负载均衡模块信息
 * create by lorne on 2017/12/5
 */
public class LoadBalanceInfo {

    private String groupId;

    private String key;

    private String data;


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
