package com.codingapi.txlcn.protocol.message.event;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.separate.TransactionMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

/**
 * @author lorne
 * @date 2020/4/3
 * @description
 */
@Data
@Slf4j
public class TransactionCreateEvent extends TransactionMessage {

    private String result;

    @Override
    public void handle(ApplicationContext springContext, Protocoler protocoler, Connection connection) throws Exception {
        super.handle(springContext, protocoler, connection);
        log.info("request msg =>{}",groupId);
        this.result = "ok";
        protocoler.sendMsg(connection.getUniqueKey(),this);
    }
}
