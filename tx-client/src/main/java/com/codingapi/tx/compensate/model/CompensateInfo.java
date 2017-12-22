package com.codingapi.tx.compensate.model;

/**
 * create by lorne on 2017/11/13
 */
public class CompensateInfo {


    private long currentTime;
    private String modelName;
    private String uniqueKey;
    private String data;
    private String methodStr;
    private String className;
    private String groupId;
    private String address;
    private long time;
    private String resJson;
    private int startError;

    private int state;


    public String toParamsString() {
        String postParam = "model=" + modelName + "&uniqueKey=" + uniqueKey + "" +
            "&address=" + address + "&currentTime=" + currentTime +
            "&data=" + data + "&time=" + time + "&groupId=" + groupId + "" +
            "&className=" + className + "&methodStr=" + methodStr+"&startError="+startError;
        return postParam;
    }

    public CompensateInfo() {
    }

    public CompensateInfo(long currentTime, String modelName, String uniqueKey, String data,
                          String methodStr, String className, String groupId, String address,
                          long time,int startError) {
        this.currentTime = currentTime;
        this.modelName = modelName;
        this.uniqueKey = uniqueKey;
        this.data = data;
        this.methodStr = methodStr;
        this.className = className;
        this.groupId = groupId;
        this.address = address;
        this.time = time;
        this.state = 0;
        this.startError =startError;
    }


    public int getStartError() {
        return startError;
    }

    public void setStartError(int startError) {
        this.startError = startError;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMethodStr() {
        return methodStr;
    }

    public void setMethodStr(String methodStr) {
        this.methodStr = methodStr;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getResJson() {
        return resJson;
    }

    public void setResJson(String resJson) {
        this.resJson = resJson;
    }
}
