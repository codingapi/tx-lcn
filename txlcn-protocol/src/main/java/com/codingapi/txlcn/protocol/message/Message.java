package com.codingapi.txlcn.protocol.message;


import com.codingapi.txlcn.protocol.Protocoler;
import org.springframework.context.ApplicationContext;

import java.io.Serializable;

/**
 * Interfaces of the messages dispatched between peers in the network
 */
public interface Message extends Serializable {


    /**
     * receive message handler, msg is this obj.
     * @param springContext springContext.
     * @param protocoler self protocoler
     * @param connection remote connection
     */
    void handle(ApplicationContext springContext, Protocoler protocoler, Connection connection) throws Exception;;


}
