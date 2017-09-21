package com.lorne.tx.mq.model;

import com.alibaba.fastjson.JSONObject;
import com.lorne.core.framework.model.JsonModel;
import com.lorne.tx.service.model.ChannelSender;
import io.netty.channel.Channel;

/**
 * Created by lorne on 2017/6/7.
 */
public class TxInfo extends JsonModel {

    private String kid;

    private String modelName;

    private int notify;

    /**
     * 0 不组合
     * 1 组合
     */
    private int isGroup;

    /**
     * tm识别标示
     */
    private String address;

    /**
     * tx识别标示
     */
    private String uniqueKey;

    private ChannelSender channel;


    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public ChannelSender getChannel() {
        return channel;
    }

    public void setChannel(ChannelSender channel) {
        this.channel = channel;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getNotify() {
        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }

    public int getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(int isGroup) {
        this.isGroup = isGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("kid",getKid());
        jsonObject.put("modelName",getModelName());
        jsonObject.put("notify",getNotify());
        jsonObject.put("isGroup",getIsGroup());
        jsonObject.put("address",getAddress());
        jsonObject.put("uniqueKey",getUniqueKey());
        return jsonObject.toString();
    }
}
