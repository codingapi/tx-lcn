package com.codingapi.tx.client.support.common;

import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.client.support.common.template.TransactionCleanTemplate;
import com.codingapi.tx.client.support.message.RpcExecuteService;
import com.codingapi.tx.client.support.message.TransactionCmd;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.commons.exception.TxClientException;
import com.codingapi.tx.client.spi.message.params.NotifyUnitParams;

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
            if (DTXLocal.cur() != null) {
                synchronized (DTXLocal.cur()) {
                    DTXLocal.cur().wait();
                }
            }
            NotifyUnitParams notifyUnitParams = transactionCmd.getMsg().loadData(NotifyUnitParams.class);
            transactionCleanTemplate.clean(
                    notifyUnitParams.getGroupId(),
                    notifyUnitParams.getUnitId(),
                    notifyUnitParams.getUnitType(),
                    notifyUnitParams.getState());
            return null;
        } catch (SerializerException | TransactionClearException | InterruptedException e) {
            throw new TxClientException(e);
        }
    }
}
