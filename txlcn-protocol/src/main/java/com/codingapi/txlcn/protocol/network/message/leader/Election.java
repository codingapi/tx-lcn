package com.codingapi.txlcn.protocol.network.message.leader;


import com.codingapi.txlcn.protocol.IPeer;
import com.codingapi.txlcn.protocol.Peer;
import com.codingapi.txlcn.protocol.network.Connection;
import com.codingapi.txlcn.protocol.network.message.Message;

/**
 * Notifies other peers about the election started by this peer
 */
public class Election implements Message {

    private static final long serialVersionUID = 3025595002500496571L;

    @Override
    public void handle(IPeer peer, Connection connection) {
        peer.optional(Peer.class).ifPresent((p)->p.handleElection(connection));
    }

}
