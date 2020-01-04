package com.codingapi.txlcn.protocol.network.message;



import com.codingapi.txlcn.protocol.Peer;
import com.codingapi.txlcn.protocol.network.Connection;

import java.io.Serializable;

/**
 * Interfaces of the messages dispatched between peers in the network
 */
public interface Message extends Serializable {

    /**
     * receive message handler, msg is this obj.
     * @param peer self peer
     * @param connection remote connection
     */
    void handle(Peer peer, Connection connection);

}
