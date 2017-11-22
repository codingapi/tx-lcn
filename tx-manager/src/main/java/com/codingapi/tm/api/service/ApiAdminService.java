package com.codingapi.tm.api.service;

import com.codingapi.tm.compensate.model.TxModel;
import com.codingapi.tm.model.ModelName;
import com.codingapi.tm.model.TxState;
import com.lorne.core.framework.exception.ServiceException;

import java.util.List;

/**
 * create by lorne on 2017/11/12
 */
public interface ApiAdminService {

    TxState getState();

    String loadNotifyJson();

    List<ModelName> modelList();


    List<String> modelTimes(String model);

    List<TxModel> modelInfos(String path);

    boolean compensate(String path) throws ServiceException;

    boolean hasCompensate();

    boolean delCompensate(String path);

}
