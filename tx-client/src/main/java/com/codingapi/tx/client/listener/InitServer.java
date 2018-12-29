package com.codingapi.tx.client.listener;

import com.codingapi.tx.client.config.TxClientConfig;
import com.codingapi.tx.spi.rpc.RpcClientInitializer;
import com.codingapi.tx.spi.rpc.dto.TxManagerHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author lorne
 * @date 2018/11/29
 * @description
 */
@Component
public class InitServer   {

    private final RpcClientInitializer rpcClientInitializer;

    private final TxClientConfig txClientConfig;

    @Autowired
    public InitServer(RpcClientInitializer rpcClientInitializer, TxClientConfig txClientConfig) {
        this.rpcClientInitializer = rpcClientInitializer;
        this.txClientConfig = txClientConfig;
    }

    @PostConstruct
    public void start(){
        rpcClientInitializer.init(TxManagerHost.parserList(txClientConfig.getManagerHost()));
    }
}
