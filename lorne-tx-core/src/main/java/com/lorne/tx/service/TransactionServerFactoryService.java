package com.lorne.tx.service;

import com.lorne.tx.bean.TxTransactionInfo;

/**
 * Created by lorne on 2017/6/8.
 */
public interface TransactionServerFactoryService {

    TransactionServer createTransactionServer(TxTransactionInfo info) throws Throwable;
}
