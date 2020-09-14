package com.codingapi.txlcn.protocol.message.separate;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationContext;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020-8-16 14:23:54
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SnowflakeMessage extends AbsMessage {

    @Override
    public void handle(ApplicationContext springContext, Protocoler protocoler, Connection connection) throws Exception {
        super.handle(springContext, protocoler, connection);
    }
}
