package com.codingapi.txlcn.tc.runner;

import com.codingapi.txlcn.protocol.client.PeerClientHandle;
import com.codingapi.txlcn.tc.config.TxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class TcRunnerConfiguration {

    @Bean
    public TMServerRunner tmServerRunner(TxConfig txConfig, PeerClientHandle peerClientHandle){
        return new TMServerRunner(txConfig, peerClientHandle);
    }

    @Autowired
    private TMServerRunner tmServerRunner;

    @PostConstruct
    public void start(){
        tmServerRunner.init();
    }

}
