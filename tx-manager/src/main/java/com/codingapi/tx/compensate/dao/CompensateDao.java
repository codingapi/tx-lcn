package com.codingapi.tx.compensate.dao;

import com.codingapi.tx.compensate.model.TransactionCompensateMsg;

/**
 * create by lorne on 2017/11/11
 */
public interface CompensateDao {

    boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg);

}
