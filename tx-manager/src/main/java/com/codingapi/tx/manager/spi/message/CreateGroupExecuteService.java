package com.codingapi.tx.manager.spi.message;

import com.codingapi.tx.commons.util.Transactions;
import com.codingapi.tx.logger.TxLogger;
import com.codingapi.tx.manager.support.group.GroupRelationship;
import com.codingapi.tx.manager.support.message.RpcExecuteService;
import com.codingapi.tx.manager.support.message.TransactionCmd;
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

    private final GroupRelationship groupRelationship;

    private final TxLogger txLogger;

    @Autowired
    public CreateGroupExecuteService(GroupRelationship groupRelationship, TxLogger txLogger) {
        this.groupRelationship = groupRelationship;
        this.txLogger = txLogger;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) {

        groupRelationship.createGroup(transactionCmd.getMsg().getGroupId());

        // log
        txLogger.trace(transactionCmd.getGroupId(), "", Transactions.TAG_TRANSACTION, "create group");
        return null;
    }
}
