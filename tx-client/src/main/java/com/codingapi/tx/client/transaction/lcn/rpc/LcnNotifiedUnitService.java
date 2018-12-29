package com.codingapi.tx.client.transaction.lcn.rpc;

import com.codingapi.tx.client.transaction.common.DefaultNotifiedUnitService;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxClientException;
import com.codingapi.tx.client.framework.rpc.RpcExecuteService;
import com.codingapi.tx.client.framework.rpc.TransactionCmd;
import com.codingapi.tx.client.transaction.common.template.TransactionCleanTemplate;
import com.codingapi.tx.commons.rpc.params.NotifyUnitParams;
import com.codingapi.tx.commons.util.serializer.ProtostuffSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Date: 2018/12/11
 *
 * @author ujued
 */
@Service("rpc_lcn_notify-unit")
@Slf4j
public class LcnNotifiedUnitService extends DefaultNotifiedUnitService {

    @Autowired
    public LcnNotifiedUnitService(TransactionCleanTemplate transactionCleanTemplate, ProtostuffSerializer protostuffSerializer) {
        super(transactionCleanTemplate, protostuffSerializer);
    }
}
