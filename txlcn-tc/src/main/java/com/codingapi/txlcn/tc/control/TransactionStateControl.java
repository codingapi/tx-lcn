package com.codingapi.txlcn.tc.control;

import com.codingapi.txlcn.tc.event.coordinator.TransactionCoordinatorListener;
import com.codingapi.txlcn.tc.info.TransactionInfo;
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

    private TransactionCoordinatorListener transactionCoordinatorListener;

    private TransactionStepExecuter transactionStepExecuter;


    public void tryBeginTransaction(TransactionInfo transactionInfo) throws Exception {
        if(!transactionInfo.isTransmitTransaction()){
            //创建事务
            log.info("create tx-transaction ");
            transactionCoordinatorListener.onBeforeCreateTransaction(transactionInfo);
            transactionStepExecuter.execute(new TransactionStep(transactionInfo, TransactionStep.Step.CREATE));
            transactionCoordinatorListener.onAfterCreateTransaction(transactionInfo);
        }
    }

    public void tryEndTransaction(TransactionInfo transactionInfo) throws Exception {
        if(transactionInfo.isTransmitTransaction()){
            // 加入事务
            log.info("join tx-transaction ");
            transactionCoordinatorListener.onBeforeJoinTransaction(transactionInfo);
            transactionStepExecuter.execute(new TransactionStep(transactionInfo, TransactionStep.Step.JOIN));
            transactionCoordinatorListener.onAfterJoinTransaction(transactionInfo);
        }else{
            // 提交事务
            log.info("notify tx-transaction ");
            transactionCoordinatorListener.onBeforeNotifyTransaction(transactionInfo);
            transactionStepExecuter.execute(new TransactionStep(transactionInfo, TransactionStep.Step.NOTIFY));
            transactionCoordinatorListener.onAfterNotifyTransaction(transactionInfo);

        }
    }
}
