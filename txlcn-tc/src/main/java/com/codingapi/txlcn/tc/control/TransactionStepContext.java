package com.codingapi.txlcn.tc.control;

import com.codingapi.maven.uml.annotation.GraphRelation;
import com.codingapi.maven.uml.annotation.Model;
import com.codingapi.txlcn.tc.info.TransactionInfo;

import java.util.List;
import java.util.Optional;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Model(flag = "I",value = "事务步骤环境",color = "#FF88EE")
public class TransactionStepContext {

    @GraphRelation(value = "*-->",type = TransactionStep.class)
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
        return Optional.ofNullable(null);
    }

    @GraphRelation(value = "..>",type = TransactionInfo.class)
    public void execute(TransactionInfo transactionInfo){
        Optional<TransactionStep> transactionStep =  transactionStep(transactionInfo.getTransactionState());
        transactionStep.ifPresent(step->step.run(transactionInfo));
    }


}

