package com.codingapi.txlcn.protocol.network.message.ping;

import com.codingapi.txlcn.protocol.IPeer;
import com.codingapi.txlcn.protocol.Peer;
import com.codingapi.txlcn.protocol.network.Connection;
import com.codingapi.txlcn.protocol.network.message.Message;

public class CancelPings implements Message {

    private static final long serialVersionUID = -8650899535821394626L;

    private String peerName;

    public CancelPings(String peerName) {
        this.peerName = peerName;
    }

    @Override
    public void handle(IPeer peer, Connection connection) {
        peer.optional(Peer.class).ifPresent((p)->p.cancelPings(connection, peerName));
    }

    @Override
    public String toString() {
        return "RemovePings{" +
                "peerName='" + peerName + '\'' +
                '}';
    }

}
