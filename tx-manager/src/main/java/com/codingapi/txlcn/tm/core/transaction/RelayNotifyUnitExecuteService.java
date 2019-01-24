package com.codingapi.txlcn.tm.core.transaction;

import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.params.RelayNotifyUnitParams;
import com.codingapi.txlcn.spi.message.util.MessageUtils;
import com.codingapi.txlcn.tm.core.message.MessageCreator;
import com.codingapi.txlcn.tm.core.message.RpcExecuteService;
import com.codingapi.txlcn.tm.core.message.TransactionCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Date: 19-1-24 下午5:39
 *
 * @author ujued
 */
@Component("rpc_relay-notify-unit")
public class RelayNotifyUnitExecuteService implements RpcExecuteService {

    private final RpcClient rpcClient;

    @Autowired
    public RelayNotifyUnitExecuteService(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxManagerException {
        RelayNotifyUnitParams notifyUnitParams = transactionCmd.getMsg().loadBean(RelayNotifyUnitParams.class);
        List<String> rpcKeys = rpcClient.remoteKeys(notifyUnitParams.getModId());
        if (rpcKeys.isEmpty()) {
            throw new TxManagerException("empty");
        }
        try {
            MessageDto messageDto = rpcClient.request(rpcKeys.get(0), MessageCreator.notifyUnit(notifyUnitParams.getNotifyUnitParams()));
            if (MessageUtils.statusOk(messageDto)) {
                return true;
            }
            throw new TxManagerException();
        } catch (RpcException e) {
            throw new TxManagerException(e);
        }
    }
}
