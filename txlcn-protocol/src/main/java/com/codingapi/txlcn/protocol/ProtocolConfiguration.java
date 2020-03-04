package com.codingapi.txlcn.protocol;

import com.codingapi.txlcn.protocol.config.Config;
import com.codingapi.txlcn.protocol.server.ProtocolServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Configuration
public class ProtocolConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "tx.lcn.protocol")
    @ConditionalOnMissingBean
    public Config config(){
        return new Config();
    }

    @Bean
    @ConditionalOnMissingBean
    public ProtocolServer protocolServer(Config config){
        return new ProtocolServer(config);

    }


}
