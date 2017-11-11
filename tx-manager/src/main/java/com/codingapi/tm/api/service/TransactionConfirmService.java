package com.codingapi.tm.api.service;

import com.codingapi.tm.listener.model.TxGroup;

/**
 * Created by lorne on 2017/6/9.
 */
public interface TransactionConfirmService {

    boolean confirm(TxGroup group);
}
