package com.codingapi.txlcn.protocol.manager.network.message.leader;

import com.codingapi.txlcn.protocol.IPeer;
import com.codingapi.txlcn.protocol.manager.Peer;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;

/**
 * Notifies the peer ,which started the election, that this peer rejected its election
 */
public class Rejection implements Message {

    private static final long serialVersionUID = -4458007227538796558L;

    @Override
    public void handle(IPeer peer, Connection connection) {
        peer.optional(Peer.class).ifPresent((p)->p.handleRejection(connection));
    }

}
