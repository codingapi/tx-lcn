package com.codingapi.tx.manager.spi.rpc;

import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.commons.rpc.params.TxExceptionParams;
import com.codingapi.tx.commons.util.serializer.ProtostuffSerializer;
import com.codingapi.tx.manager.support.rpc.RpcExecuteService;
import com.codingapi.tx.manager.support.TransactionCmd;
import com.codingapi.tx.manager.core.service.TxExceptionService;
import com.codingapi.tx.manager.core.service.WriteTxExceptionDTO;
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

    private final ProtostuffSerializer protostuffSerializer;

    private final TxExceptionService compensationService;

    @Autowired
    public WriteTxExceptionExecuteService(ProtostuffSerializer protostuffSerializer,
                                          TxExceptionService compensationService) {
        this.protostuffSerializer = protostuffSerializer;
        this.compensationService = compensationService;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        try {
            TxExceptionParams txExceptionParams =
                    protostuffSerializer.deSerialize(transactionCmd.getMsg().getBytes(), TxExceptionParams.class);
            WriteTxExceptionDTO writeTxExceptionReq = new WriteTxExceptionDTO();
            writeTxExceptionReq.setClientAddress(transactionCmd.getRemoteKey());
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
