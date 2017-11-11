package com.codingapi.tx.listener.service;

import com.codingapi.tx.listener.model.Request;

/**
 * Created by lorne on 2017/6/30.
 */
public interface NettyService {

    void start();

    void close();

    String sendMsg(Request request);

    boolean checkState();

}
