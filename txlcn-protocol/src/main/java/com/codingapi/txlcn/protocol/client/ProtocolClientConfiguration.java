package com.codingapi.txlcn.protocol.client;

import com.codingapi.txlcn.protocol.PeerEventLoopGroup;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProtocolClientConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public TCHandle peerClientHandle(PeerEventLoopGroup peerEventLoopGroup) {
    return new TCHandle(peerEventLoopGroup);
  }

}
