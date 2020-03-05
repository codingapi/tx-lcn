package com.codingapi.txlcn.tc.control;

import java.util.List;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
public class TransactionStepExecuter {

    private List<TransactionStepRunner> transactionStepRunners;

    public TransactionStepExecuter(List<TransactionStepRunner> transactionStepRunners) {
        this.transactionStepRunners = transactionStepRunners;
    }

    public void execute(TransactionStep transactionStep){
        for(TransactionStepRunner transactionStepRunner:transactionStepRunners){
            if(transactionStepRunner.step().equals(transactionStep.getStep())) {
                transactionStepRunner.run(transactionStep.getTransactionInfo());
            }
        }
    }

}
