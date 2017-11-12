package com.codingapi.tm.api.service;

import com.codingapi.tm.model.TxState;

/**
 * create by lorne on 2017/11/12
 */
public interface ApiAdminService {

    TxState getState();

    String loadNotifyJson();
}
