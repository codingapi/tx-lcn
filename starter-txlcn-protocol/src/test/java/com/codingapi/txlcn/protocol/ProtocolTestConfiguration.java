package com.codingapi.txlcn.protocol;

import com.codingapi.txlcn.protocol.config.PeerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProtocolTestConfiguration {


    @Bean
    public PeerConfig peerConfig(){
        PeerConfig peerConfig = new PeerConfig();
        peerConfig.setName("peer1");
        peerConfig.setPort(8092);
        return peerConfig;
    }

}
