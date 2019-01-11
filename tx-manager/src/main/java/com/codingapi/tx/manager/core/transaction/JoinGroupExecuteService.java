package com.codingapi.tx.manager.core.transaction;

import com.codingapi.tx.client.spi.message.params.JoinGroupParams;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.logger.TxLogger;
import com.codingapi.tx.manager.core.GroupTransaction;
import com.codingapi.tx.manager.core.ex.TransactionException;
import com.codingapi.tx.manager.core.TransactionManager;
import com.codingapi.tx.manager.core.TransactionUnit;
import com.codingapi.tx.manager.core.message.RpcExecuteService;
import com.codingapi.tx.manager.core.message.TransactionCmd;
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

    private final TxLogger txLogger;


    @Autowired
    public JoinGroupExecuteService(TxLogger txLogger, TransactionManager transactionManager) {
        this.txLogger = txLogger;
        this.transactionManager = transactionManager;
    }


    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {

            txLogger.trace(transactionCmd.getGroupId(), "", "", "start join group");

            JoinGroupParams joinGroupParams = transactionCmd.getMsg().loadData(JoinGroupParams.class);
            TransactionUnit transactionUnit = new TransactionUnit(joinGroupParams.getUnitId(), joinGroupParams.getUnitType(), transactionCmd.getRemoteKey());
            transactionManager.join(new GroupTransaction(transactionCmd.getGroupId()), transactionUnit);

            txLogger.trace(transactionCmd.getGroupId(), joinGroupParams.getUnitId(), "", "over join group");

        } catch (SerializerException | TransactionException e) {
            throw new TxManagerException(e.getLocalizedMessage());
        }
        return null;
    }
}
