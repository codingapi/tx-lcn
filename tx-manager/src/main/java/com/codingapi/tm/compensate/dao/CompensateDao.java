package com.codingapi.tm.compensate.dao;

import com.codingapi.tm.compensate.model.TransactionCompensateMsg;

import java.util.List;

/**
 * create by lorne on 2017/11/11
 */
public interface CompensateDao {

    String saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg);

    List<String> loadCompensateKeys();

    List<String> loadCompensateTimes(String model);

    List<String> loadCompensateByModelAndTime(String path);

    String getCompensate(String key);

    String getCompensateByGroupId(String groupId);

    void deleteCompensateByPath(String path);

    void deleteCompensateByKey(String key);

    boolean hasCompensate();
}
