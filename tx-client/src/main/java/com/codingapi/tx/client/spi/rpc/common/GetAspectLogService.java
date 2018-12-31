package com.codingapi.tx.client.spi.rpc.common;

import com.alibaba.fastjson.JSON;
import com.codingapi.tx.client.support.rpc.RpcExecuteService;
import com.codingapi.tx.client.support.rpc.TransactionCmd;
import com.codingapi.tx.client.aspectlog.AspectLogHelper;
import com.codingapi.tx.client.aspectlog.AspectLog;
import com.codingapi.tx.commons.bean.TransactionInfo;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxClientException;
import com.codingapi.tx.commons.rpc.params.GetAspectLogParams;
import com.codingapi.tx.commons.util.serializer.ProtostuffSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/29
 *
 * @author ujued
 */
@Component("rpc_get-aspect-log")
public class GetAspectLogService implements RpcExecuteService {

    @Autowired
    private ProtostuffSerializer protostuffSerializer;

    @Autowired
    private AspectLogHelper txLogHelper;

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxClientException {
        try {
            GetAspectLogParams getAspectLogParams = protostuffSerializer.deSerialize(transactionCmd.getMsg().getBytes(), GetAspectLogParams.class);
            AspectLog txLog = txLogHelper.getTxLog(getAspectLogParams.getGroupId(), getAspectLogParams.getUnitId());
            if (Objects.isNull(txLog)) {
                throw new TxClientException("non exists aspect log.");
            }
            return JSON.toJSON(protostuffSerializer.deSerialize(txLog.getBytes(), TransactionInfo.class));
        } catch (SerializerException e) {
            throw new TxClientException(e);
        }
    }
}
