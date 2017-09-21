package com.lorne.tx.service;

import com.lorne.tx.mq.model.TxGroup;

/**
 * Created by lorne on 2017/6/9.
 */
public interface TransactionConfirmService {

    void confirm(TxGroup group);
}
