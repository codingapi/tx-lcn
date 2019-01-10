package com.codingapi.tx.manager;

import com.codingapi.tx.manager.config.TxManagerConfig;
import com.codingapi.tx.client.springcloud.spi.message.RpcServerInitializer;
import com.codingapi.tx.client.springcloud.spi.message.dto.ManagerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/11/29
 *
 * @author lorne
 */
@Component
public class TxManagerInitialization {

    private final TxManagerConfig txManagerConfig;

    private final RpcServerInitializer rpcServerInitializer;

    @Autowired
    public TxManagerInitialization(TxManagerConfig txManagerConfig, RpcServerInitializer rpcServerInitializer) {
        this.txManagerConfig = txManagerConfig;
        this.rpcServerInitializer = rpcServerInitializer;
    }

    @PostConstruct
    public void start(){
        ManagerProperties managerProperties = new ManagerProperties();
        managerProperties.setCheckTime(txManagerConfig.getHeartTime());
        managerProperties.setRpcPort(txManagerConfig.getPort());
        rpcServerInitializer.init(managerProperties);
    }
}
