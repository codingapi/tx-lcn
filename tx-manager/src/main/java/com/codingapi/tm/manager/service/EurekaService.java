package com.codingapi.tm.manager.service;

import com.codingapi.tm.model.TxServer;
import com.codingapi.tm.model.TxState;
import com.netflix.appinfo.InstanceInfo;

import java.util.List;

/**
 * create by lorne on 2017/11/11
 */
public interface EurekaService {

    TxServer getServer();

    TxState getState();

    List<InstanceInfo> getConfigServiceInstances();
}
