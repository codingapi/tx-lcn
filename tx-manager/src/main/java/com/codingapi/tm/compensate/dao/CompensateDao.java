package com.codingapi.tm.compensate.dao;

import com.codingapi.tm.compensate.model.TransactionCompensateMsg;

import java.util.List;

/**
 * create by lorne on 2017/11/11
 */
public interface CompensateDao {

    boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg);

    List<String> loadModelList();


    List<String> childModel(String model);

    List<String> logFile(String path);

    List<String> getLogs(String path);
}
