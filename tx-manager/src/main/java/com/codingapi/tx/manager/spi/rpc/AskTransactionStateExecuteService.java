package com.codingapi.tx.manager.spi.rpc;

import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.commons.rpc.params.AskTransactionStateParams;
import com.codingapi.tx.commons.util.serializer.ProtostuffSerializer;
import com.codingapi.tx.manager.support.group.GroupRelationship;
import com.codingapi.tx.manager.support.rpc.RpcExecuteService;
import com.codingapi.tx.manager.support.TransactionCmd;
import com.codingapi.tx.manager.core.service.TxExceptionService;
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

    private final TxExceptionService compensationService;

    private final ProtostuffSerializer protostuffSerializer;

    @Autowired
    private GroupRelationship groupRelationship;

    @Autowired
    public AskTransactionStateExecuteService(TxExceptionService compensationService,
                                             ProtostuffSerializer protostuffSerializer) {
        this.compensationService = compensationService;
        this.protostuffSerializer = protostuffSerializer;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            AskTransactionStateParams askTransactionStateParams =
                    protostuffSerializer.deSerialize(transactionCmd.getMsg().getBytes(),
                            AskTransactionStateParams.class);

            short state = compensationService.transactionUnitState(askTransactionStateParams.getGroupId(),
                    askTransactionStateParams.getUnitId());

            if (state != -1) {
                return state;
            }
            return groupRelationship.transactionState(transactionCmd.getGroupId());
        } catch (SerializerException e) {
            throw new TxManagerException(e.getMessage());
        }
    }
}
