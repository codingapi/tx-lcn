package com.codingapi.tx.manager.listener;


import com.codingapi.tx.manager.config.TxManagerConfig;
import com.codingapi.tx.spi.rpc.RpcServerInitializer;
import com.codingapi.tx.spi.rpc.dto.ManagerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author lorne
 * @date 2018/11/29
 * @description
 */
@Component
public class InitServer {

    @Autowired
    private TxManagerConfig txManagerConfig;

    @Autowired
    private RpcServerInitializer rpcServerInitializer;

    @PostConstruct
    public void start(){
        ManagerProperties managerProperties = new ManagerProperties();
        managerProperties.setCheckTime(txManagerConfig.getHeartTime());
        managerProperties.setRpcPort(txManagerConfig.getRpcPort());
        rpcServerInitializer.init(managerProperties);
    }
}
