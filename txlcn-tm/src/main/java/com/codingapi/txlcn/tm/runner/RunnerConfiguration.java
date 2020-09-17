package com.codingapi.txlcn.tm.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RunnerConfiguration {

    @Autowired
    private ProtocolRunner protocolRunner;

    @Autowired
    private TmNodeServerRunner tmNodeServerRunner;

    @Bean
    public ProtocolRunner protocolRunner(ProtocolServer protocolServer) {
        return new ProtocolRunner(protocolServer);
    }

    @Bean
    public TmNodeServerRunner tmNodeServerRunner(ProtocolServer protocolServer) {
        return new TmNodeServerRunner(protocolServer);
    }

    @PostConstruct
    public void start() {
        protocolRunner.start();
        tmNodeServerRunner.init();
    }



}
