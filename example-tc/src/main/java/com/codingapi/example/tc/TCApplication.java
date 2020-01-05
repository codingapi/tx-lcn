package com.codingapi.example.tc;

import com.codingapi.txlcn.protocol.client.PeerClient;
import com.codingapi.txlcn.protocol.client.PeerClientHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class TCApplication {

    public static void main(String[] args) {
        SpringApplication.run(TCApplication.class,args);
    }

    @Autowired
    private PeerClientHandle cliet;

    @PostConstruct
    public void init() throws InterruptedException{
        PeerClient peerClient = new PeerClient("127.0.0.1",8070);
        cliet.connectTo(peerClient,"TM");
    }
}
