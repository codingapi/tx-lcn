package com.codingapi.txlcn.tm.runner;

import com.codingapi.txlcn.protocol.PeerHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RunnerConfiguration {

    @Bean
    public ProtocolRunner protocolRunner(PeerHandle peerHandle){
        return new ProtocolRunner(peerHandle);
    }

    @Autowired
    private ProtocolRunner protocolRunner;

    @PostConstruct
    public void start(){
        protocolRunner.start();
    }

}
