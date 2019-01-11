package com.codingapi.tx.manager.config;

<<<<<<< HEAD:tx-manager/src/main/java/com/codingapi/tx/manager/config/MyRpcConfig.java
import com.codingapi.tx.spi.message.RpcConfig;
=======
import com.codingapi.tx.client.springcloud.spi.message.netty.MessageConfig;
>>>>>>> 4a50e1937323b1e4422f87687c88cc6401007275:tx-manager/src/main/java/com/codingapi/tx/manager/config/NettyMessageConfig.java
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
