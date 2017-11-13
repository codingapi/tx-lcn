package com.codingapi.tm.compensate.model;

/**
 * create by lorne on 2017/11/12
 */
public class TxModel {

    private String time;

    private String className;

    private String method;

    private int executeTime;

    private String base64;

    private int state;

    private long order;

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(int executeTime) {
        this.executeTime = executeTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
