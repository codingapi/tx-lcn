package com.lorne.tx.service.model;

/**
 * Created by lorne on 2017/7/1.
 */
public class TxServer {

    private String ip;
    private int port;
    private int heart;
    private int delay;

    public static TxServer format(TxState state) {
        TxServer txServer = new TxServer();
        txServer.setIp(state.getIp());
        txServer.setPort(state.getPort());
        txServer.setHeart(state.getTransactionNettyHeartTime());
        txServer.setDelay(state.getTransactionNettyDelayTime());
        return txServer;
    }


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
}
