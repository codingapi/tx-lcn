package com.codingapi.tm.model;

import java.util.List;

/**
 * Created by lorne on 2017/7/1.
 */
public class TxState {

    /**
     * socket ip
     */
    private String ip;
    /**
     * socket port
     */
    private int port;

    /**
     * max connection
     */
    private int maxConnection;

    /**
     * now connection
     */
    private int nowConnection;


    /**
     *  transaction_netty_heart_time
     */
    private int transactionNettyHeartTime;

    /**
     *  transaction_netty_delay_time
     */
    private int transactionNettyDelayTime;


    /**
     * redis_save_max_time
     */
    private int redisSaveMaxTime;


    /**
     * 回调地址
     */
    private String notifyUrl;

    /**
     * 自动补偿
     */
    private boolean isCompensate;

    /**
     * 补偿尝试时间
     */
    private int compensateTryTime;

    /**
     * slb list
     */
    private List<String> slbList;
    
    /**
     * 自动补偿间隔时间
     */
    private int compensateMaxWaitTime;


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }

    public int getNowConnection() {
        return nowConnection;
    }

    public void setNowConnection(int nowConnection) {
        this.nowConnection = nowConnection;
    }

    public boolean isCompensate() {
        return isCompensate;
    }

    public void setCompensate(boolean compensate) {
        isCompensate = compensate;
    }

    public int getCompensateTryTime() {
        return compensateTryTime;
    }

    public void setCompensateTryTime(int compensateTryTime) {
        this.compensateTryTime = compensateTryTime;
    }

    public int getRedisSaveMaxTime() {
        return redisSaveMaxTime;
    }

    public void setRedisSaveMaxTime(int redisSaveMaxTime) {
        this.redisSaveMaxTime = redisSaveMaxTime;
    }

    public List<String> getSlbList() {
        return slbList;
    }

    public void setSlbList(List<String> slbList) {
        this.slbList = slbList;
    }

    public int getTransactionNettyHeartTime() {
        return transactionNettyHeartTime;
    }

    public void setTransactionNettyHeartTime(int transactionNettyHeartTime) {
        this.transactionNettyHeartTime = transactionNettyHeartTime;
    }

    public int getTransactionNettyDelayTime() {
        return transactionNettyDelayTime;
    }

    public void setTransactionNettyDelayTime(int transactionNettyDelayTime) {
        this.transactionNettyDelayTime = transactionNettyDelayTime;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

	public int getCompensateMaxWaitTime() {
		return compensateMaxWaitTime;
	}

	public void setCompensateMaxWaitTime(int compensateMaxWaitTime) {
		this.compensateMaxWaitTime = compensateMaxWaitTime;
	}
    
    
}
