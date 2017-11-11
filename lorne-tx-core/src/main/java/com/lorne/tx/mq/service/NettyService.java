package com.lorne.tx.mq.service;

import com.lorne.tx.mq.model.Request;

/**
 * Created by lorne on 2017/6/30.
 */
public interface NettyService {

    void start();

    void close();

    void restart();

    String sendMsg(Request request);

    boolean checkState();

//    long checkCompensate(String taskId);
//
//    void executeCompensate(String taskId);
}
