package com.codingapi.txlcn.tc.control;

import com.codingapi.txlcn.tc.state.TransactionState;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Slf4j
public class TransactionStateControl {


    public void tryBeginTransaction(TransactionState transactionState) {
        if(!transactionState.isTransmitTransaction()){
            //创建事务
            log.info("create tx-transaction ");
        }
    }

    public void tryEndTransaction(TransactionState transactionState) {
        if(transactionState.isTransmitTransaction()){
            // 加入事务
            log.info("join tx-transaction ");
        }else{
            // 提交事务
            log.info("commit tx-transaction ");
        }
    }
}
