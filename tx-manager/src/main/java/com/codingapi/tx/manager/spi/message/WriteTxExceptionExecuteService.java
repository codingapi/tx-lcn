package com.codingapi.tx.manager.spi.message;

import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.manager.core.service.TxExceptionService;
import com.codingapi.tx.manager.core.service.WriteTxExceptionDTO;
import com.codingapi.tx.manager.support.message.RpcExecuteService;
import com.codingapi.tx.manager.support.message.TransactionCmd;
import com.codingapi.tx.client.springcloud.spi.message.RpcClient;
import com.codingapi.tx.client.springcloud.spi.message.params.TxExceptionParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/20
 *
 * @author ujued
 */
@Component("rpc_write-compensation")
@Slf4j
public class WriteTxExceptionExecuteService implements RpcExecuteService {

    private final TxExceptionService compensationService;

    private final RpcClient rpcClient;

    @Autowired
    public WriteTxExceptionExecuteService(TxExceptionService compensationService, RpcClient rpcClient) {
        this.compensationService = compensationService;
        this.rpcClient = rpcClient;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            TxExceptionParams txExceptionParams = transactionCmd.getMsg().loadData(TxExceptionParams.class);
            WriteTxExceptionDTO writeTxExceptionReq = new WriteTxExceptionDTO();
            writeTxExceptionReq.setModId(rpcClient.getAppName(transactionCmd.getRemoteKey()));
            writeTxExceptionReq.setTransactionState(txExceptionParams.getTransactionState());
            writeTxExceptionReq.setGroupId(txExceptionParams.getGroupId());
            writeTxExceptionReq.setUnitId(txExceptionParams.getUnitId());
            writeTxExceptionReq.setRegistrar(Objects.isNull(txExceptionParams.getRegistrar()) ? -1 : txExceptionParams.getRegistrar());
            compensationService.writeTxException(writeTxExceptionReq);
        } catch (SerializerException e) {
            throw new TxManagerException(e);
        }
        return null;
    }
}
