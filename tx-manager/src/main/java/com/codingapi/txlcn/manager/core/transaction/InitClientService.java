package com.codingapi.txlcn.manager.core.transaction;

import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.exception.TxManagerException;
import com.codingapi.txlcn.manager.config.TxManagerConfig;
import com.codingapi.txlcn.manager.core.message.RpcExecuteService;
import com.codingapi.txlcn.manager.core.message.TransactionCmd;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.params.InitClientParams;
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
    private RpcClient rpcClient;

    @Autowired
    private TxManagerConfig txManagerConfig;


    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxManagerException {
        log.info("init client - >{}",transactionCmd);
        try {
            InitClientParams initClientParams = transactionCmd.getMsg().loadData(InitClientParams.class);
            rpcClient.bindAppName(transactionCmd.getRemoteKey(),initClientParams.getAppName());
            initClientParams.setDtxTime(txManagerConfig.getDtxTime());
            return initClientParams;
        } catch (SerializerException e) {
            throw new TxManagerException(e);
        }
    }
}
