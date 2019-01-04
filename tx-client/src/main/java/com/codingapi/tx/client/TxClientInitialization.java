package com.codingapi.tx.client;

import com.codingapi.tx.client.config.TxClientConfig;
import com.codingapi.tx.spi.rpc.RpcClientInitializer;
import com.codingapi.tx.spi.rpc.dto.TxManagerHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author lorne
 */
@Component
public class TxClientInitialization {

    private final RpcClientInitializer rpcClientInitializer;

    private final TxClientConfig txClientConfig;

    @Autowired
    public TxClientInitialization(RpcClientInitializer rpcClientInitializer, TxClientConfig txClientConfig) {
        this.rpcClientInitializer = rpcClientInitializer;
        this.txClientConfig = txClientConfig;
    }

    @PostConstruct
    public void start(){
        rpcClientInitializer.init(TxManagerHost.parserList(txClientConfig.getManagerAddress()));
    }
}
