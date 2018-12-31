package com.codingapi.tx.client.support.common;

import com.codingapi.tx.client.support.common.template.TransactionCleanTemplate;
import com.codingapi.tx.client.support.rpc.RpcExecuteService;
import com.codingapi.tx.client.support.rpc.TransactionCmd;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.commons.exception.TxClientException;
import com.codingapi.tx.commons.rpc.params.NotifyUnitParams;
import com.codingapi.tx.commons.util.serializer.ProtostuffSerializer;

/**
 * Description: 默认RPC命令业务
 * Date: 2018/12/20
 *
 * @author ujued
 */
public class DefaultNotifiedUnitService implements RpcExecuteService {

    private final TransactionCleanTemplate transactionCleanTemplate;

    private final ProtostuffSerializer protostuffSerializer;

    public DefaultNotifiedUnitService(TransactionCleanTemplate transactionCleanTemplate,
                                      ProtostuffSerializer protostuffSerializer) {
        this.transactionCleanTemplate = transactionCleanTemplate;
        this.protostuffSerializer = protostuffSerializer;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxClientException {
        try {
            NotifyUnitParams notifyUnitParams =
                    protostuffSerializer.deSerialize(transactionCmd.getMsg().getBytes(), NotifyUnitParams.class);
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
