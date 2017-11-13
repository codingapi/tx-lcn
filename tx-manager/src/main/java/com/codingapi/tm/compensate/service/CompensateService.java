package com.codingapi.tm.compensate.service;

import com.codingapi.tm.compensate.model.TransactionCompensateMsg;
import com.codingapi.tm.compensate.model.TxModel;
import com.lorne.core.framework.exception.ServiceException;

import java.util.List;

/**
 * create by lorne on 2017/11/11
 */
public interface CompensateService {

    boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg);

    List<String> loadModelList();

    List<String> loadCompensateTimes(String model);

    List<TxModel> loadCompensateByModelAndTime(String path);


    boolean executeCompensate(String key) throws ServiceException;

}
