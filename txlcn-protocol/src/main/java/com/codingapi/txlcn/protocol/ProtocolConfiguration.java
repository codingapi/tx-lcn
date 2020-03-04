package com.codingapi.txlcn.protocol;

import com.codingapi.txlcn.protocol.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
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
    @ConfigurationProperties(prefix = "txlcn.protocol")
    @ConditionalOnMissingBean
    public Config config(){
        return new Config();
    }

    @Bean
    @ConditionalOnMissingBean
    public ProtocolServer protocolServer(Config config, ApplicationContext applicationContext){
        return new ProtocolServer(config,applicationContext);

    }


}
