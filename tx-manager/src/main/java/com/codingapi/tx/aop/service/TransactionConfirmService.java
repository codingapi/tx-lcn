package com.codingapi.tx.aop.service;

import com.codingapi.tx.listener.model.TxGroup;

/**
 * Created by lorne on 2017/6/9.
 */
public interface TransactionConfirmService {

    boolean confirm(TxGroup group);
}
