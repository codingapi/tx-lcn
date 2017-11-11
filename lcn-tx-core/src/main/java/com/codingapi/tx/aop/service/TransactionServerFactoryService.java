package com.codingapi.tx.aop.service;

import com.codingapi.tx.aop.bean.TxTransactionInfo;

/**
 * Created by lorne on 2017/6/8.
 */
public interface TransactionServerFactoryService {

    TransactionServer createTransactionServer(TxTransactionInfo info) throws Throwable;
}
