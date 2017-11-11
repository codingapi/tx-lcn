package com.codingapi.tx.listener.model;


import com.alibaba.fastjson.JSONObject;

/**
 * Created by lorne on 2017/6/30.
 */
public class TxServer {

    private int port;
    private String host;
    private int heart;
    private int delay;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    @Override
    public String toString() {
        return "host:" + host + ",port:" + port + ",heart:" + heart + ",delay:" + delay;
    }

    public static TxServer parser(String json) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(json);
            TxServer txServer = new TxServer();
            txServer.setPort(jsonObject.getInteger("port"));
            txServer.setHost(jsonObject.getString("ip"));
            txServer.setHeart(jsonObject.getInteger("heart"));
            txServer.setDelay(jsonObject.getInteger("delay"));
            return txServer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
