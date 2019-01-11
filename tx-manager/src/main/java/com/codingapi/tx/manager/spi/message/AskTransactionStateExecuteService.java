package com.codingapi.tx.manager.spi.message;

import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.client.spi.message.params.AskTransactionStateParams;
import com.codingapi.tx.manager.support.group.GroupRelationship;
import com.codingapi.tx.manager.support.message.RpcExecuteService;
import com.codingapi.tx.manager.support.message.TransactionCmd;
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


    @Autowired
    private GroupRelationship groupRelationship;

    @Autowired
    public AskTransactionStateExecuteService(TxExceptionService compensationService ) {
        this.compensationService = compensationService;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            AskTransactionStateParams askTransactionStateParams =transactionCmd.getMsg().loadData(AskTransactionStateParams.class);

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
