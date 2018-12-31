package com.codingapi.tx.client.spi.rpc.common;

import com.codingapi.tx.client.support.rpc.RpcExecuteService;
import com.codingapi.tx.client.support.rpc.TransactionCmd;
import com.codingapi.tx.commons.exception.SerializerException;
import com.codingapi.tx.commons.exception.TxClientException;
import com.codingapi.tx.spi.rpc.params.NotifyConnectParams;
import com.codingapi.tx.spi.rpc.RpcClientInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Service(value = "rpc_notify-connect")
@Slf4j
public class RpcNotifyConnectService implements RpcExecuteService {



    @Autowired
    private RpcClientInitializer rpcClientInitializer;

    @Override
    public Object execute(TransactionCmd transactionCmd) throws TxClientException {
        try {

            log.info("transactionCmd->{}", transactionCmd);

            NotifyConnectParams notifyConnectParams = transactionCmd.getMsg().loadData(NotifyConnectParams.class);

            log.info("notifyConnectParams->{}", notifyConnectParams);

            rpcClientInitializer.connect(new InetSocketAddress(notifyConnectParams.getHost(), notifyConnectParams.getPort()));
        } catch (SerializerException e) {
            throw new TxClientException(e);
        }

        return null;
    }
}
