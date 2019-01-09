package com.codingapi.tx.manager.config;

import com.codingapi.tx.spi.message.netty.MessageConfig;
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
public class NettyMessageConfig implements InitializingBean {

    @Autowired
    private MessageConfig messageConfig;

    @Autowired
    private TxManagerConfig managerConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        messageConfig.setAttrDelayTime(managerConfig.getDtxTime() / 1000);
    }
}
