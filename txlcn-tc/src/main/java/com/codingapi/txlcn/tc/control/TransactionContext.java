package com.codingapi.txlcn.tc.control;

import com.codingapi.txlcn.tc.event.transaction.TransactionEventContext;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.jdbc.JdbcTransaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Slf4j
@AllArgsConstructor
public class TransactionContext {

    private TransactionEventContext transactionEventContext;

    private TransactionStepContext transactionStepContext;

    public void tryBeginTransaction(TransactionInfo transactionInfo) throws Exception {
        if(transactionInfo.isState(TransactionState.CREATE)){
            //创建事务
            log.info("create tx-transaction ");
            transactionEventContext.onBeforeCreateTransaction(transactionInfo);
            transactionStepContext.execute(transactionInfo);
            transactionEventContext.onAfterCreateTransaction(transactionInfo);
        }
    }

    public void tryEndTransaction(TransactionInfo transactionInfo) throws Exception {
        //状态判定
        if(transactionInfo.isState(TransactionState.CREATE)){
            transactionInfo.setTransactionState(TransactionState.NOTIFY);
        }

        if(transactionInfo.isState(TransactionState.NOTIFY)){

            // 提交事务
            log.info("notify tx-transaction ");
            transactionEventContext.onBeforeNotifyTransaction(transactionInfo);
            transactionStepContext.execute(transactionInfo);
            transactionEventContext.onAfterNotifyTransaction(transactionInfo);

        }else{
            // 加入事务
            log.info("join tx-transaction ");
            transactionEventContext.onBeforeJoinTransaction(transactionInfo);
            transactionStepContext.execute(transactionInfo);
            transactionEventContext.onAfterJoinTransaction(transactionInfo);
        }

    }


    public void clearTransaction(){
        JdbcTransaction.clear();
        TransactionInfo.clear();
    }
}
