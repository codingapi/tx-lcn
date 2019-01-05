package com.codingapi.tx.client.support.common;

import com.codingapi.tx.client.support.common.template.TransactionCleanTemplate;
import com.codingapi.tx.client.support.rpc.RpcExecuteService;
import com.codingapi.tx.client.support.rpc.TransactionCmd;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.commons.exception.TxClientException;
import com.codingapi.tx.spi.message.params.NotifyUnitParams;

/**
 * Description: 默认RPC命令业务
 * Date: 2018/12/20
 *
 * @author ujued
 */
public class DefaultNotifiedUnitService implements RpcExecuteService {

    private final TransactionCleanTemplate transactionCleanTemplate;


    public DefaultNotifiedUnitService(TransactionCleanTemplate transactionCleanTemplate) {
        this.transactionCleanTemplate = transactionCleanTemplate;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxClientException {
        try {
            NotifyUnitParams notifyUnitParams =transactionCmd.getMsg().loadData(NotifyUnitParams.class);
            transactionCleanTemplate.clean(
                    notifyUnitParams.getGroupId(),
                    notifyUnitParams.getUnitId(),
                    notifyUnitParams.getUnitType(),
                    notifyUnitParams.getState());
            return null;
        } catch (SerializerException | TransactionClearException e) {
            throw new TxClientException(e);
        }
    }
}
