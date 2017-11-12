package com.codingapi.tm.api.service;

import com.codingapi.tm.compensate.model.TxModel;
import com.codingapi.tm.model.TxState;

import java.util.List;

/**
 * create by lorne on 2017/11/12
 */
public interface ApiAdminService {

    TxState getState();

    String loadNotifyJson();

    List<String> modelList();


    List<String> childModel(String model);

    List<String> logFile(String path);

    List<TxModel> logs(String path);
}
