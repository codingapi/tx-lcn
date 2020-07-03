package com.codingapi.txlcn.tc.control.step;

import com.codingapi.txlcn.protocol.message.event.TransactionNotifyEvent;
import com.codingapi.txlcn.tc.control.TransactionCommitorStrategy;
import com.codingapi.txlcn.tc.control.TransactionState;
import com.codingapi.txlcn.tc.control.TransactionStep;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.reporter.TxManagerReporter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2020/3/5
 * @description 通知步骤的业务逻辑
 *
 */
@Slf4j
@AllArgsConstructor
public class TransactionStepNotify implements TransactionStep {

    private TxManagerReporter managerProtocoler;

    private TransactionCommitorStrategy transactionCommitorStrategy;

    @Override
    public TransactionState type() {
        return TransactionState.NOTIFY;
    }

    @Override
    public void run(TransactionInfo transactionInfo) {
        long t1 = System.currentTimeMillis();
        TransactionNotifyEvent res = (TransactionNotifyEvent) managerProtocoler.requestMsg(new TransactionNotifyEvent(transactionInfo.getGroupId(),transactionInfo.getSuccessReturn()));

        while (transactionInfo.getSuccessReturn()&&res==null){
            //尝试再次通知.
            res = (TransactionNotifyEvent) managerProtocoler.requestMsg(new TransactionNotifyEvent(transactionInfo.getGroupId(),transactionInfo.getSuccessReturn()));
        }
        //当TM返回是正常时,则需要提交本地事务.
        transactionCommitorStrategy.commit(res != null && (res.isSuccess() && transactionInfo.getSuccessReturn()));

        long t2 = System.currentTimeMillis();
        log.info("notify transaction result:{},time:{}",res,(t2-t1));


    }
}
