package com.codingapi.txlcn.tc.control.step;

import com.codingapi.maven.uml.annotation.GraphRelation;
import com.codingapi.maven.uml.annotation.Model;
import com.codingapi.txlcn.protocol.message.event.TransactionNotifyEvent;
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
@Model(flag = "C",value = "事务步骤-通知事务",color = "#FF88EE")
@GraphRelation(value = "..|>",type = TransactionStep.class)
public class TransactionStepNotify implements TransactionStep {

    @GraphRelation(value = "-->",type = TxManagerReporter.class)
    private TxManagerReporter managerProtocoler;

    @Override
    public TransactionState type() {
        return TransactionState.NOTIFY;
    }

    @Override
    public void run(TransactionInfo transactionInfo) {
        long t1 = System.currentTimeMillis();
        TransactionNotifyEvent res = (TransactionNotifyEvent) managerProtocoler.requestMsg(new TransactionNotifyEvent(transactionInfo.getGroupId(),transactionInfo.getSuccessReturn()));

        if(res==null){
            throw new TxException("notify transaction fail.");
        }
        long t2 = System.currentTimeMillis();
        log.info("notify transaction result:{},time:{}",res.getResult(),(t2-t1));
    }
}
