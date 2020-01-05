package com.codingapi.txlcn.protocol.client.service;

import com.codingapi.txlcn.protocol.client.PeerClient;
import com.codingapi.txlcn.protocol.message.Connection;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PeerClientConnectionService {

    private final Map<String, PeerClient> clients = new HashMap<String, PeerClient>();

    public Collection<PeerClient> clients() {
        return Collections.unmodifiableCollection(clients.values());
    }

    public void remove(Connection connection) {
        clients.remove(connection.getPeerName());
    }

    public void add(PeerClient peerClient) {
        clients.put(peerClient.getPeerName(), peerClient);
    }
}
