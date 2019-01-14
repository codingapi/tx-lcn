package com.codingapi.txlcn.manager.core.transaction;

import com.codingapi.txlcn.spi.message.params.JoinGroupParams;
import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.manager.core.context.DTXTransaction;
import com.codingapi.txlcn.manager.core.context.DTXTransactionContext;
import com.codingapi.txlcn.commons.exception.TransactionException;
import com.codingapi.txlcn.manager.core.context.TransactionManager;
import com.codingapi.txlcn.manager.core.group.TransactionUnit;
import com.codingapi.txlcn.manager.core.message.RpcExecuteService;
import com.codingapi.txlcn.manager.core.message.TransactionCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author ujued
 */
@Service("rpc_join-group")
@Slf4j
public class JoinGroupExecuteService implements RpcExecuteService {

    private final TransactionManager transactionManager;

    private final DTXTransactionContext transactionContext;

    private final TxLogger txLogger;


    @Autowired
    public JoinGroupExecuteService(TxLogger txLogger, TransactionManager transactionManager,
                                   DTXTransactionContext transactionContext) {
        this.txLogger = txLogger;
        this.transactionManager = transactionManager;
        this.transactionContext = transactionContext;
    }


    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        DTXTransaction dtxTransaction = transactionContext.getTransaction(transactionCmd.getGroupId());
        try {
            JoinGroupParams joinGroupParams = transactionCmd.getMsg().loadData(JoinGroupParams.class);
            txLogger.trace(
                    transactionCmd.getGroupId(), joinGroupParams.getUnitId(), Transactions.TAG_TRANSACTION, "start join group");
            TransactionUnit transactionUnit =
                    new TransactionUnit(joinGroupParams.getUnitId(), joinGroupParams.getUnitType(), transactionCmd.getRemoteKey());
            transactionManager.join(dtxTransaction, transactionUnit);

            txLogger.trace(
                    transactionCmd.getGroupId(), joinGroupParams.getUnitId(), Transactions.TAG_TRANSACTION, "over join group");
        } catch (SerializerException | TransactionException e) {
            throw new TxManagerException(e.getLocalizedMessage());
        }

        // non response
        return null;
    }
}
