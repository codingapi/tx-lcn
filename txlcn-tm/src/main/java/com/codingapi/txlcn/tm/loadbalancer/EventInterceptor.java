package com.codingapi.txlcn.tm.loadbalancer;

import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.separate.AbsMessage;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020/9/9 17:48
 */
public interface EventInterceptor {

    void handle(AbsMessage absMessage, Protocoler protocoler, Connection connection, EventService event) throws Exception;
}
