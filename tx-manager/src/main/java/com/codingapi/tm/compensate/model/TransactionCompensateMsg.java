package com.codingapi.tm.compensate.model;

import com.codingapi.tm.netty.model.TxGroup;

/**
 * create by lorne on 2017/11/11
 */
public class TransactionCompensateMsg {

    private String groupId;
    private String model;
    private String address;
    private String uniqueKey;
    private String className;
    private String method;
    private String data;
    private int time;

    private TxGroup txGroup;


    public TransactionCompensateMsg(String groupId, String model, String address,
                                    String uniqueKey, String className,
                                    String method, String data, int time) {
        this.groupId = groupId;
        this.model = model;
        this.uniqueKey = uniqueKey;
        this.className = className;
        this.method = method;
        this.data = data;
        this.time = time;
        this.address = address;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TxGroup getTxGroup() {
        return txGroup;
    }

    public void setTxGroup(TxGroup txGroup) {
        this.txGroup = txGroup;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
