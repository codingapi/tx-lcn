package com.codingapi.txlcn.protocol.message;


import com.codingapi.txlcn.protocol.Protocoler;

import java.io.Serializable;

/**
 * Interfaces of the messages dispatched between peers in the network
 */
public interface Message extends Serializable {


    /**
     * receive message handler, msg is this obj.
     * @param protocoler self protocoler
     * @param connection remote connection
     */
    void handle(Protocoler protocoler, Connection connection) throws Exception;;


}
