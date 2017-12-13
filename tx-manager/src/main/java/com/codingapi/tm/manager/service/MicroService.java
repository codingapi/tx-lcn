package com.codingapi.tm.manager.service;

import com.codingapi.tm.model.TxServer;
import com.codingapi.tm.model.TxState;

/**
 * create by lorne on 2017/11/11
 */
public interface MicroService {

    String  tmKey = "tx-manager";

    TxServer getServer();

    TxState getState();
}
