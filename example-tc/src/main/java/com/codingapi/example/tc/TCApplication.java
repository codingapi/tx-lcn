package com.codingapi.example.tc;

import com.codingapi.txlcn.protocol.client.PeerClientHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication
public class TCApplication {

    public static void main(String[] args) {
        SpringApplication.run(TCApplication.class,args);
    }

    @Autowired
    private PeerClientHandle peerClient;

    @PostConstruct
    public void init() throws InterruptedException{
        peerClient.connectTo("TC1","127.0.0.1",8070);
        peerClient.connectTo("TC1","127.0.0.1",8070);
        peerClient.connectTo("TC1","127.0.0.1",8070);

//        Thread.sleep(5000);
//
//        List<PeerClient> clients = peerClient.list();
//
//        for(PeerClient client:clients){
//            client.close();
//            log.info("peerClient.clients()->{}",peerClient.list());
//            Thread.sleep(1000);
//        }

    }
}
