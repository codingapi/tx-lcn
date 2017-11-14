package com.codingapi.tx.model;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

/**
 * Created by lorne on 2017/6/7.
 */
public class TxGroup {

    private String groupId;

    private long startTime;

    private long nowTime;

    private int hasOver;

    private int isCommit;

    public int getIsCommit() {
        return isCommit;
    }

    public void setIsCommit(int isCommit) {
        this.isCommit = isCommit;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
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

    public static TxGroup parser(String json) {
        try {
            if (StringUtils.isEmpty(json)) {
                return null;
            }
            JSONObject jsonObject = JSONObject.parseObject(json);
            TxGroup txGroup = new TxGroup();
            txGroup.setGroupId(jsonObject.getString("g"));
            txGroup.setStartTime(jsonObject.getLong("st"));
            txGroup.setHasOver(jsonObject.getInteger("o"));
            txGroup.setNowTime(jsonObject.getLong("nt"));
            txGroup.setIsCommit(jsonObject.getInteger("i"));
            return txGroup;

        } catch (Exception e) {
            return null;
        }

    }

    public String toJsonString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("g", getGroupId());
        jsonObject.put("st", getStartTime());
        jsonObject.put("o",getHasOver());
        jsonObject.put("nt", getNowTime());
        jsonObject.put("i", getIsCommit());
        JSONArray jsonArray = new JSONArray();
        jsonObject.put("l", jsonArray);
        return jsonObject.toString();
    }
}
