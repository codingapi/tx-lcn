package com.codingapi.tx.manager.core;

import com.codingapi.tx.client.spi.message.RpcClient;
import com.codingapi.tx.client.spi.message.dto.MessageDto;
import com.codingapi.tx.client.spi.message.exception.RpcException;
import com.codingapi.tx.client.spi.message.params.NotifyUnitParams;
import com.codingapi.tx.client.spi.message.util.MessageUtils;
import com.codingapi.tx.commons.exception.JoinGroupException;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.util.serializer.SerializerContext;
import com.codingapi.tx.manager.core.ex.TransactionException;
import com.codingapi.tx.manager.core.group.GroupRelationship;
import com.codingapi.tx.manager.core.group.TransUnit;
import com.codingapi.tx.manager.core.message.MessageCreator;
import com.codingapi.tx.manager.core.message.RpcExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Date: 19-1-11 下午5:57
 *
 * @author ujued
 */
@Slf4j
@Component
public class DefaultTransactionManager implements TransactionManager {

    private final GroupRelationship groupRelationship;

    private final RpcExceptionHandler rpcExceptionHandler;

    @Autowired
    private RpcClient rpcClient;

    @Autowired
    public DefaultTransactionManager(GroupRelationship groupRelationship, RpcExceptionHandler rpcExceptionHandler) {
        this.groupRelationship = groupRelationship;
        this.rpcExceptionHandler = rpcExceptionHandler;
    }

    @Override
    public void begin(GroupTransaction dtxTransaction) {
        groupRelationship.createGroup(dtxTransaction.groupId());
    }

    @Override
    public void join(GroupTransaction dtxTransaction, TransactionUnit transactionUnit) throws TransactionException {
        TransUnit transUnit = new TransUnit();
        transUnit.setRemoteKey(transactionUnit.messageContextId());
        transUnit.setUnitType(transactionUnit.unitType());
        transUnit.setUnitId(transactionUnit.unitId());
        log.info("unit:{} joined group:{}", transactionUnit.unitId(), dtxTransaction.groupId());
        try {
            groupRelationship.joinGroup(dtxTransaction.groupId(), transUnit);
        } catch (JoinGroupException e) {
            throw new TransactionException(e);
        }
    }

    @Override
    public void commit(GroupTransaction transaction) {
        notifyTransaction(transaction.groupId(), 1);
    }

    @Override
    public void rollback(GroupTransaction transaction) {
        notifyTransaction(transaction.groupId(), 0);
    }

    @Override
    public void close(GroupTransaction groupTransaction) {
        groupRelationship.removeGroup(groupTransaction.groupId());
    }

    private void notifyTransaction(String groupId, int transactionState) {
        groupRelationship.setTransactionState(groupId, transactionState);
        List<TransUnit> transUnits = groupRelationship.unitsOfGroup(groupId);
        for (TransUnit transUnit : transUnits) {
            NotifyUnitParams notifyUnitParams = new NotifyUnitParams();
            notifyUnitParams.setGroupId(groupId);
            notifyUnitParams.setUnitId(transUnit.getUnitId());
            notifyUnitParams.setUnitType(transUnit.getUnitType());
            notifyUnitParams.setState(transactionState);

            try {
                MessageDto respMsg =
                        rpcClient.request(transUnit.getRemoteKey(), MessageCreator.notifyUnit(notifyUnitParams));
                log.debug("notify unit: {}", transUnit.getRemoteKey());
                if (!MessageUtils.statusOk(respMsg)) {
                    // 提交/回滚失败的消息处理
                    List<Object> params = Arrays.asList(notifyUnitParams, transUnit.getRemoteKey());
                    rpcExceptionHandler.handleNotifyUnitBusinessException(params, throwable(respMsg.getBytes()));
                }
            } catch (RpcException | SerializerException e) {
                // 提交/回滚通讯失败
                List<Object> params = Arrays.asList(notifyUnitParams, transUnit.getRemoteKey());
                rpcExceptionHandler.handleNotifyUnitMessageException(params, e);
            }
        }
    }

    private Throwable throwable(byte[] data) throws SerializerException {
        return SerializerContext.getInstance().deSerialize(data, Throwable.class);
    }
}
