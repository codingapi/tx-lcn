package com.codingapi.tx.service;

import com.codingapi.tx.mq.model.TxGroup;

/**
 * Created by lorne on 2017/6/9.
 */
public interface TransactionConfirmService {

    boolean confirm(TxGroup group);
}
