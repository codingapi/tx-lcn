package com.codingapi.txlcn.tc.control.step;

import com.codingapi.txlcn.protocol.message.event.TransactionCreateEvent;
import com.codingapi.txlcn.tc.control.TransactionState;
import com.codingapi.txlcn.tc.control.TransactionStep;
import com.codingapi.txlcn.tc.exception.TxException;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.reporter.TxManagerReporter;
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
public class TransactionStepCreate implements TransactionStep {

    private TxManagerReporter managerProtocoler;

    @Override
    public TransactionState type() {
        return TransactionState.CREATE;
    }

    @Override
    public void run(TransactionInfo transactionInfo) {
        long t1 = System.currentTimeMillis();
        TransactionCreateEvent res = (TransactionCreateEvent) managerProtocoler.requestMsg(new TransactionCreateEvent(transactionInfo.getGroupId()));
        if(res==null){
            throw new TxException("create transaction fail.");
        }
        long t2 = System.currentTimeMillis();
        log.info("create transaction result:{},time:{}",res.getResult(),(t2-t1));
    }
}
