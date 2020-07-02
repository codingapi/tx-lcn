package com.codingapi.txlcn.tc.control;

import com.codingapi.txlcn.tc.info.TransactionInfo;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
public interface TransactionStep {

    TransactionState type();

    void run(TransactionInfo transactionInfo);

}
