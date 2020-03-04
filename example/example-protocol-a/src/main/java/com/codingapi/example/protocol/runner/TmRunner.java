package com.codingapi.example.protocol.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Configuration
public class TmRunner {

    @Autowired
    private ProtocolServer protocolServer;

    @PostConstruct
    public void start(){
        try {
            protocolServer.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
