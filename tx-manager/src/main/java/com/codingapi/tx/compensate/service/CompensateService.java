package com.codingapi.tx.compensate.service;

import com.codingapi.tx.compensate.model.TransactionCompensateMsg;

/**
 * create by lorne on 2017/11/11
 */
public interface CompensateService {

    boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg);
}
