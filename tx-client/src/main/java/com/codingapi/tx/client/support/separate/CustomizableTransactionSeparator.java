package com.codingapi.tx.client.support.separate;

import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.commons.util.DTXFunctions;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: 可定制的事务分离器
 * Date: 2018/12/5
 *
 * @author ujued
 */
@Slf4j
public class CustomizableTransactionSeparator implements TXLCNTransactionSeparator {

    @Override
    public TXLCNTransactionState loadTransactionState(TxTransactionInfo txTransactionInfo) {

        // 本线程已经参与分布式事务(本地方法互调)
        if (DTXLocal.cur().isInUnit()) {
            log.info("Default by DTXLocal is not null! {}", DTXLocal.cur());
            return TXLCNTransactionState.DEFAULT;
        }

        // 发起分布式事务条件
        if (txTransactionInfo.isTransactionStart()) {
            // 发起方时，对于只加入DTX的事务单元走默认处理
            if (txTransactionInfo.getTransactionFunc() == DTXFunctions.JOIN) {
                return TXLCNTransactionState.DEFAULT;
            }
            return TXLCNTransactionState.STARTING;
        }


        // 加入分布式事务
        return TXLCNTransactionState.RUNNING;
    }
}
