package com.codingapi.txlcn.tc.support.listener;

import com.codingapi.txlcn.tc.core.TxTransactionInfo;

/**
 * Description: 事务流程监听器
 * Date: 19-1-25 下午1:27
 *
 * @author ujued
 */
public interface TransactionListener {

    void onTransactionBegin(TxTransactionInfo txTransactionInfo);

    void onTransactionError(TxTransactionInfo txTransactionInfo);

    void onTransactionClean(String groupId, String unitId, int transactionState);
}
