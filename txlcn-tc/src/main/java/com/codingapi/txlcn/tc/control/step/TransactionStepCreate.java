package com.codingapi.txlcn.tc.control.step;

import com.codingapi.maven.uml.annotation.GraphRelation;
import com.codingapi.maven.uml.annotation.Model;
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
@Model(flag = "C",value = "事务步骤-创建事务",color = "#FF88EE")
public class TransactionStepCreate implements TransactionStep {

    @GraphRelation(value = "-->",type = TxManagerReporter.class)
    private TxManagerReporter managerProtocoler;

    @Override
    public TransactionState type() {
        return TransactionState.CREATE;
    }

    @Override
    public void run(TransactionInfo transactionInfo) {
        TransactionCreateEvent res = (TransactionCreateEvent) managerProtocoler.requestMsg(new TransactionCreateEvent(transactionInfo.getGroupId()));
        if(res==null){
            throw new TxException("create transaction fail.");
        }
        log.info("create transaction ... {}",res.getResult());
    }
}
