package com.codingapi.txlcn.protocol.client.service;

import com.codingapi.txlcn.protocol.client.PeerClient;
import com.codingapi.txlcn.protocol.message.Connection;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class PeerClientConnectionService {

    private final List<PeerClient> clients = new CopyOnWriteArrayList<>();

    public List<PeerClient> clients() {
        return clients;
    }

    public void remove(Connection connection) {
        String peerName = connection.getPeerName();
        for(PeerClient client:clients){
            if(client.getKey().equals(peerName)){
                clients.remove(client);
            }
        }
    }

    public void add(PeerClient peerClient) {
        clients.add(peerClient);
    }
}
