package com.codingapi.txlcn.protocol.client.network.message;

import com.codingapi.txlcn.protocol.IPeer;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;

/**
 * heartbeat message,send with tc client .
 */
public class Heartbeat implements Message {

    @Override
    public void handle(IPeer peer, Connection connection) {
        //TM no any message to response.
    }
}
