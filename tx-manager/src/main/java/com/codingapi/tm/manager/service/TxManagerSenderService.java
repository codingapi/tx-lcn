package com.codingapi.tm.manager.service;

import com.codingapi.tm.netty.model.TxGroup;

/**
 * Created by lorne on 2017/6/9.
 */
public interface TxManagerSenderService {

    int confirm(TxGroup group);

    String sendMsg(String model, String msg, int delay);

    String sendCompensateMsg(String model, String groupId, String data,int startState);
}
