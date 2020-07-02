package com.codingapi.txlcn.tc.control;

import com.codingapi.txlcn.tc.info.TransactionInfo;

import java.util.List;
import java.util.Optional;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
public class TransactionStepContext {

    private List<TransactionStep> transactionSteps;

    public TransactionStepContext(List<TransactionStep> transactionSteps) {
        this.transactionSteps = transactionSteps;
    }

    private Optional<TransactionStep> transactionStep(TransactionState type) {
        for (TransactionStep transactionStep : transactionSteps) {
            if (transactionStep.type().equals(type)) {
                return Optional.of(transactionStep);
            }
        }
        return Optional.empty();
    }

    public void execute(TransactionInfo transactionInfo){
        Optional<TransactionStep> transactionStep =  transactionStep(transactionInfo.getTransactionState());
        transactionStep.ifPresent(step->step.run(transactionInfo));
    }


}

