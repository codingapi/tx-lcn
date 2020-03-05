package com.codingapi.txlcn.tc.control;

import com.codingapi.txlcn.tc.event.coordinator.CoordinatorListener;
import com.codingapi.txlcn.tc.state.TransactionState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Slf4j
@AllArgsConstructor
public class TransactionStateControl {


    private CoordinatorListener coordinatorListener;


    public void tryBeginTransaction(TransactionState transactionState) throws Exception {
        if(!transactionState.isTransmitTransaction()){
            //创建事务
            log.info("create tx-transaction ");
            coordinatorListener.onBeforeCreateTransaction(null);

            coordinatorListener.onAfterCreateTransaction(null,null);
        }
    }

    public void tryEndTransaction(TransactionState transactionState) throws Exception {
        if(transactionState.isTransmitTransaction()){
            // 加入事务
            log.info("join tx-transaction ");
            coordinatorListener.onBeforeJoinTransaction(null);

            coordinatorListener.onAfterJoinTransaction(null,null);
        }else{
            // 提交事务
            log.info("commit tx-transaction ");
            coordinatorListener.onBeforeNotifyTransaction(null);

            coordinatorListener.onAfterNotifyTransaction(null,null);

        }
    }
}
