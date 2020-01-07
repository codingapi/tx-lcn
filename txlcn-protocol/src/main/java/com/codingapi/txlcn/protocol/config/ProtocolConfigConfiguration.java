package com.codingapi.txlcn.protocol.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProtocolConfigConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "txlcn.protocol")
  @ConditionalOnMissingBean
  public PeerConfig peerConfig() {
    return new PeerConfig();
  }

}
