package com.codingapi.txlcn.protocol.message.separate;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;
import lombok.Data;
import org.springframework.context.ApplicationContext;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Data
public abstract class AbsMessage implements Message {

    private Boolean isFirstNode;

    @Override
    public void handle(ApplicationContext springContext,
                       Protocoler protocoler,
                       Connection connection) throws Exception {

    }
}
