package com.lorne.tx.compensate.dao;

import com.lorne.tx.compensate.model.TransactionCompensateMsg;

/**
 * create by lorne on 2017/11/11
 */
public interface CompensateDao {

    boolean saveCompensateMsg(TransactionCompensateMsg transactionCompensateMsg);

}
