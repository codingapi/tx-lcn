package com.codingapi.tx.manager.core.transaction;

import com.codingapi.tx.commons.util.Transactions;
import com.codingapi.tx.logger.TxLogger;
import com.codingapi.tx.manager.core.GroupTransaction;
import com.codingapi.tx.manager.core.TransactionManager;
import com.codingapi.tx.manager.core.message.RpcExecuteService;
import com.codingapi.tx.manager.core.message.TransactionCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author ujued
 */
@Service("rpc_create-group")
public class CreateGroupExecuteService implements RpcExecuteService {

    private final TxLogger txLogger;

    private final TransactionManager transactionManager;

    @Autowired
    public CreateGroupExecuteService(TxLogger txLogger, TransactionManager transactionManager) {
        this.txLogger = txLogger;
        this.transactionManager = transactionManager;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) {
        transactionManager.begin(new GroupTransaction(transactionCmd.getGroupId()));
        // log
        txLogger.trace(transactionCmd.getGroupId(), "", Transactions.TAG_TRANSACTION, "create group");
        return null;
    }
}
