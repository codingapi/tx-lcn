package com.codingapi.txlcn.tc.control;

import com.codingapi.maven.uml.annotation.GraphRelation;
import com.codingapi.maven.uml.annotation.Model;
import com.codingapi.txlcn.tc.event.transaction.TransactionEventContext;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.jdbc.JdbcTransaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Slf4j
@AllArgsConstructor
@Model(flag = "C",value = "事务环境信息",color = "#FF88EE")
public class TransactionContext {

    @GraphRelation(value = "-->",type = TransactionEventContext.class)
    private TransactionEventContext transactionEventContext;

    @GraphRelation(value = "-->",type = TransactionStepContext.class)
    private TransactionStepContext transactionStepContext;

    @GraphRelation(value = "..>",type = TransactionInfo.class)
    public void tryBeginTransaction(TransactionInfo transactionInfo) throws Exception {
        if(transactionInfo.isState(TransactionState.CREATE)){
            //创建事务
            log.info("create tx-transaction ");
            transactionEventContext.onBeforeCreateTransaction(transactionInfo);
            transactionStepContext.execute(transactionInfo);
            transactionEventContext.onAfterCreateTransaction(transactionInfo);
        }
    }

    public void tryEndTransaction(TransactionInfo transactionInfo) throws Exception {
        //状态判定
        if(transactionInfo.isState(TransactionState.CREATE)){
            transactionInfo.setTransactionState(TransactionState.NOTIFY);
        }

        if(transactionInfo.isState(TransactionState.NOTIFY)){

            // 提交事务
            log.info("notify tx-transaction ");
            transactionEventContext.onBeforeNotifyTransaction(transactionInfo);
            transactionStepContext.execute(transactionInfo);
            transactionEventContext.onAfterNotifyTransaction(transactionInfo);

        }else{
            // 加入事务
            log.info("join tx-transaction ");
            transactionEventContext.onBeforeJoinTransaction(transactionInfo);
            transactionStepContext.execute(transactionInfo);
            transactionEventContext.onAfterJoinTransaction(transactionInfo);
        }
        JdbcTransaction.clear();
        transactionInfo.clear();
    }

    @GraphRelation(value = "..>",type = TransactionState.class)
    public TransactionState getState() {
        return null;
    }
}
