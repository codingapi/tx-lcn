package com.codingapi.txlcn.client.message;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.client.message.helper.RpcExecuteService;
import com.codingapi.txlcn.client.message.helper.TransactionCmd;
import com.codingapi.txlcn.client.aspectlog.AspectLogHelper;
import com.codingapi.txlcn.client.aspectlog.AspectLog;
import com.codingapi.txlcn.commons.bean.TransactionInfo;
import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.exception.TxClientException;
import com.codingapi.txlcn.commons.util.serializer.SerializerContext;
import com.codingapi.txlcn.spi.message.params.GetAspectLogParams;
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

    private final AspectLogHelper txLogHelper;

    @Autowired
    public GetAspectLogService(AspectLogHelper txLogHelper) {
        this.txLogHelper = txLogHelper;
    }

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxClientException {
        try {
            GetAspectLogParams getAspectLogParams =transactionCmd.getMsg().loadData(GetAspectLogParams.class);
            AspectLog txLog = txLogHelper.getTxLog(getAspectLogParams.getGroupId(), getAspectLogParams.getUnitId());
            if (Objects.isNull(txLog)) {
                throw new TxClientException("non exists aspect log.");
            }
            return JSON.toJSON(SerializerContext.getInstance().deSerialize(txLog.getBytes(), TransactionInfo.class));
        } catch (SerializerException e) {
            throw new TxClientException(e);
        }
    }
}
