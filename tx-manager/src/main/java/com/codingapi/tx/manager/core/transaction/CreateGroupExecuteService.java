package com.codingapi.tx.manager.core.transaction;

import com.codingapi.tx.commons.exception.TransactionException;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.commons.util.Transactions;
import com.codingapi.tx.logger.TxLogger;
import com.codingapi.tx.manager.core.context.DTXTransaction;
import com.codingapi.tx.manager.core.context.DTXTransactionContext;
import com.codingapi.tx.manager.core.context.TransactionManager;
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

    private final DTXTransactionContext transactionContext;

    @Autowired
    public CreateGroupExecuteService(TxLogger txLogger, TransactionManager transactionManager,
                                     DTXTransactionContext transactionContext) {
        this.txLogger = txLogger;
        this.transactionManager = transactionManager;
        this.transactionContext = transactionContext;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        DTXTransaction dtxTransaction = transactionContext.newContext(transactionCmd.getGroupId());
        try {
            transactionManager.begin(dtxTransaction);
        } catch (TransactionException e) {
            throw new TxManagerException(e);
        }
        txLogger.trace(transactionCmd.getGroupId(), "", Transactions.TAG_TRANSACTION, "create group");
        return null;
    }
}
