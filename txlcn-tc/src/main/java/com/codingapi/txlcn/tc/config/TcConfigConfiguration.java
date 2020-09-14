package com.codingapi.txlcn.tc.config;

import com.codingapi.txlcn.protocol.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TcConfigConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "txlcn.tc")
    public TxConfig txConfig(Config config) {
        return new TxConfig(config);
    }


}
