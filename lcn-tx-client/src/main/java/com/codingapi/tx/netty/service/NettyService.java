package com.codingapi.tx.netty.service;

/**
 * Created by lorne on 2017/6/30.
 */
public interface NettyService {

    void start();

    void close();

    boolean checkState();

}
