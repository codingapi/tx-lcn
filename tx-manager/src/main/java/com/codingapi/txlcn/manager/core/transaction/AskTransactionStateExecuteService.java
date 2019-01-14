package com.codingapi.txlcn.manager.core.transaction;

import com.codingapi.txlcn.manager.core.context.DTXTransactionContext;
import com.codingapi.txlcn.manager.core.context.TransactionManager;
import com.codingapi.txlcn.manager.core.message.RpcExecuteService;
import com.codingapi.txlcn.manager.core.message.TransactionCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
@Component("rpc_ask-transaction-state")
@Slf4j
public class AskTransactionStateExecuteService implements RpcExecuteService {

    private final TransactionManager transactionManager;

    private final DTXTransactionContext transactionContext;

    @Autowired
    public AskTransactionStateExecuteService(TransactionManager transactionManager, DTXTransactionContext transactionContext) {
        this.transactionManager = transactionManager;
        this.transactionContext = transactionContext;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) {
        return transactionManager.transactionState(transactionContext.getTransaction(transactionCmd.getGroupId()));
    }
}
