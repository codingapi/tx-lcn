package com.codingapi.txlcn.manager.core.transaction;

import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.manager.support.service.TxExceptionService;
import com.codingapi.txlcn.manager.support.service.WriteTxExceptionDTO;
import com.codingapi.txlcn.manager.core.message.RpcExecuteService;
import com.codingapi.txlcn.manager.core.message.TransactionCmd;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.params.TxExceptionParams;
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
