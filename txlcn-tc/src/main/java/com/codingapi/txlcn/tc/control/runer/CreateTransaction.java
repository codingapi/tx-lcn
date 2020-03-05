package com.codingapi.txlcn.tc.control.runer;

import com.codingapi.txlcn.tc.control.TransactionStep;
import com.codingapi.txlcn.tc.control.TransactionStepRunner;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Slf4j
public class CreateTransaction implements TransactionStepRunner {

    @Override
    public TransactionStep.Step step() {
        return TransactionStep.Step.CREATE;
    }

    @Override
    public void run(TransactionInfo transactionInfo) {
        log.info("create transaction ... ");
    }
}
