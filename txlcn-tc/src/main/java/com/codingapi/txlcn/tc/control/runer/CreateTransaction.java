package com.codingapi.txlcn.tc.control.runer;

import com.codingapi.txlcn.protocol.message.event.TransactionCreateEvent;
import com.codingapi.txlcn.tc.control.TransactionStep;
import com.codingapi.txlcn.tc.control.TransactionStepRunner;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.protocol.TxManagerProtocoler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2020/3/5
 * @description 创建步骤的业务逻辑
 *
 */
@Slf4j
@AllArgsConstructor
public class CreateTransaction implements TransactionStepRunner {

    private TxManagerProtocoler managerProtocoler;

    @Override
    public TransactionStep.Step step() {
        return TransactionStep.Step.CREATE;
    }

    @Override
    public void run(TransactionInfo transactionInfo) {
        managerProtocoler.sendMsg(new TransactionCreateEvent());
        log.info("create transaction ... ");
    }
}
