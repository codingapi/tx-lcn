package com.codingapi.tm.compensate.service;

import com.codingapi.tm.compensate.model.TransactionCompensateMsg;

/**
 * create by lorne on 2017/11/11
 */
public interface CompensateService {

    boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg);
}
