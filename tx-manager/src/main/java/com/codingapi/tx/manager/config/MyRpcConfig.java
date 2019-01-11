package com.codingapi.tx.manager.config;

import com.codingapi.tx.client.spi.message.RpcConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Date: 19-1-9 下午6:13
 *
 * @author ujued
 */
@Configuration
public class MyRpcConfig implements InitializingBean {

    @Autowired
    private RpcConfig rpcConfig;

    @Autowired
    private TxManagerConfig managerConfig;

    @Override
    public void afterPropertiesSet() {
        rpcConfig.setAttrDelayTime(managerConfig.getDtxTime() / 1000);
    }
}
