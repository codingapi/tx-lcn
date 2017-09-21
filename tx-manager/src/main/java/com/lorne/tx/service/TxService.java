package com.lorne.tx.service;

import com.lorne.tx.service.model.TxServer;
import com.lorne.tx.service.model.TxState;

/**
 * Created by lorne on 2017/7/1.
 */
public interface TxService {

    TxServer getServer();

    TxState getState();

    boolean sendMsg(String model,String msg);

    boolean checkClearGroup(String groupId, String taskId, int isGroup);

    int checkGroup(String groupId, String taskId);
}
