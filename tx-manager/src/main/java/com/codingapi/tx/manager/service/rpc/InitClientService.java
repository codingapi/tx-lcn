package com.codingapi.tx.manager.service.rpc;

import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxManagerException;
import com.codingapi.tx.commons.rpc.params.InitClientParams;
import com.codingapi.tx.commons.util.serializer.ProtostuffSerializer;
import com.codingapi.tx.manager.config.TxManagerConfig;
import com.codingapi.tx.manager.rpc.RpcExecuteService;
import com.codingapi.tx.manager.rpc.TransactionCmd;
import com.codingapi.tx.spi.rpc.RpcClient;
import com.codingapi.tx.spi.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Service(value = "rpc_init-client")
@Slf4j
public class InitClientService implements RpcExecuteService {


    @Autowired
    private ProtostuffSerializer protostuffSerializer;

    @Autowired
    private RpcClient rpcClient;

    @Autowired
    private TxManagerConfig txManagerConfig;


    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        log.info("init client - >{}",transactionCmd);
        try {
            InitClientParams initClientParams = protostuffSerializer.deSerialize(transactionCmd.getMsg().getBytes(), InitClientParams.class);
            rpcClient.bindAppName(transactionCmd.getRemoteKey(),initClientParams.getAppName());
            initClientParams.setDtxTime(txManagerConfig.getDtxTime());
            return initClientParams;
        } catch (SerializerException | RpcException e) {
            throw new TxManagerException(e);
        }
    }
}
