package com.codingapi.txlcn.protocol;

import com.codingapi.txlcn.protocol.config.PeerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProtocolConfiguration {

    @Bean(destroyMethod = "destroy")
    @ConditionalOnMissingBean
    public PeerEventLoopGroup peerEventLoopGroup(PeerConfig peerConfig){
        return new PeerEventLoopGroup(peerConfig.getName(),peerConfig.getPort());
    }
}
