package com.codingapi.txlcn.protocol;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
@SpringBootTest(classes = {ProtocolConfiguration.class,ProtocolTestConfiguration.class})
public class ProtocolTest {

    @Autowired
    private PeerHandle peerHandle;

    @Test
    public void peer1() throws InterruptedException {
        peerHandle.start();
    }

}