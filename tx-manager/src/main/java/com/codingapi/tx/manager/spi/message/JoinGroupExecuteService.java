package com.codingapi.tx.manager.spi.message;

import com.codingapi.tx.commons.exception.JoinGroupException;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.spi.message.params.JoinGroupParams;
import com.codingapi.tx.logger.TxLogger;
import com.codingapi.tx.manager.support.group.GroupRelationship;
import com.codingapi.tx.manager.support.group.TransUnit;
import com.codingapi.tx.manager.support.message.RpcExecuteService;
import com.codingapi.tx.manager.support.message.TransactionCmd;
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

    private final GroupRelationship groupRelationship;


    private final TxLogger txLogger;


    @Autowired
    public JoinGroupExecuteService(GroupRelationship groupRelationship, TxLogger txLogger) {
        this.groupRelationship = groupRelationship;
        this.txLogger = txLogger;
    }


    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {

            txLogger.trace(transactionCmd.getGroupId(),"","","start join group");

            JoinGroupParams joinGroupParams = transactionCmd.getMsg().loadData(JoinGroupParams.class);
            TransUnit transUnit = new TransUnit();
            transUnit.setRemoteKey(transactionCmd.getRemoteKey());
            transUnit.setUnitType(joinGroupParams.getUnitType());
            transUnit.setUnitId(joinGroupParams.getUnitId());
            log.info("unit:{} joined group:{}", joinGroupParams.getUnitId(), transactionCmd.getGroupId());
            groupRelationship.joinGroup(joinGroupParams.getGroupId(), transUnit);

            txLogger.trace(transactionCmd.getGroupId(),joinGroupParams.getUnitId(),"","over join group");

        } catch (SerializerException | JoinGroupException e) {
            throw new TxManagerException(e.getLocalizedMessage());
        }
        return null;
    }
}
