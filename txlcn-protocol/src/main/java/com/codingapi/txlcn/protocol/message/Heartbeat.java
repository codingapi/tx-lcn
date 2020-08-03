package com.codingapi.txlcn.protocol.message;

import com.codingapi.txlcn.protocol.Protocoler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Slf4j
public class Heartbeat implements Message {

    @Override
    public void handle(ApplicationContext springContext, Protocoler protocoler, Connection connection) throws Exception {
//        log.debug("heartbeat message from {}",connection);
    }
}
