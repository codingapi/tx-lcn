package com.codingapi.tm.netty.model;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lorne on 2017/6/7.
 */
public class TxGroup {

    private String groupId;

    private long startTime;

    private long nowTime;

    private int state;

    private int hasOver;

    /**
     * 补偿请求
     */
    private int isCompensate;


    /**
     * 是否强制回滚(1:开启，0:关闭)
     */
    private int rollback = 0 ;

    private List<TxInfo> list;

    public TxGroup() {
        list = new ArrayList<TxInfo>();
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<TxInfo> getList() {
        return list;
    }

    public void setList(List<TxInfo> list) {
        this.list = list;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getIsCompensate() {
        return isCompensate;
    }

    public void setIsCompensate(int isCompensate) {
        this.isCompensate = isCompensate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void addTransactionInfo(TxInfo info) {
        list.add(info);
    }

    public long getNowTime() {
        return nowTime;
    }

    public void setNowTime(long nowTime) {
        this.nowTime = nowTime;
    }


    public int getHasOver() {
        return hasOver;
    }

    public void setHasOver(int hasOver) {
        this.hasOver = hasOver;
    }

    public int getRollback() {
        return rollback;
    }

    public void setRollback(int rollback) {
        this.rollback = rollback;
    }

    public static TxGroup parser(String json) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(json);
            TxGroup txGroup = new TxGroup();
            txGroup.setGroupId(jsonObject.getString("g"));
            txGroup.setStartTime(jsonObject.getLong("st"));
            txGroup.setNowTime(jsonObject.getLong("nt"));
            txGroup.setState(jsonObject.getInteger("s"));
            txGroup.setIsCompensate(jsonObject.getInteger("i"));
            txGroup.setRollback(jsonObject.getInteger("r"));
            txGroup.setHasOver(jsonObject.getInteger("o"));
            JSONArray array = jsonObject.getJSONArray("l");
            int length = array.size();
            for (int i = 0; i < length; i++) {
                JSONObject object = array.getJSONObject(i);
                TxInfo info = new TxInfo();
                info.setKid(object.getString("k"));
                info.setChannelAddress(object.getString("ca"));
                info.setNotify(object.getInteger("n"));
                info.setIsGroup(object.getInteger("ig"));
                info.setAddress(object.getString("a"));
                info.setUniqueKey(object.getString("u"));

                info.setModel(object.getString("mn"));
                info.setModelIpAddress(object.getString("ip"));
                info.setMethodStr(object.getString("ms"));

                txGroup.getList().add(info);
            }
            return txGroup;

        } catch (Exception e) {
            return null;
        }

    }

    public String toJsonString(boolean noList) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("g", getGroupId());
        jsonObject.put("st", getStartTime());
        jsonObject.put("nt", getNowTime());
        jsonObject.put("s", getState());
        jsonObject.put("i", getIsCompensate());
        jsonObject.put("r", getRollback());
        jsonObject.put("o",getHasOver());
        if(noList) {
            JSONArray jsonArray = new JSONArray();
            for (TxInfo info : getList()) {
                JSONObject item = new JSONObject();
                item.put("k", info.getKid());
                item.put("ca", info.getChannelAddress());
                item.put("n", info.getNotify());
                item.put("ig", info.getIsGroup());
                item.put("a", info.getAddress());
                item.put("u", info.getUniqueKey());

                item.put("mn", info.getModel());
                item.put("ip", info.getModelIpAddress());
                item.put("ms", info.getMethodStr());


                jsonArray.add(item);
            }
            jsonObject.put("l", jsonArray);
        }
        return jsonObject.toString();
    }

    public String toJsonString() {
        return toJsonString(true);
    }
}
