package com.codingapi.txlcn.protocol.client.service;

import com.codingapi.txlcn.protocol.client.TCPeer;
import com.codingapi.txlcn.protocol.message.Connection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PeerClientConnectionService {

  private final List<TCPeer> clients = new CopyOnWriteArrayList<>();

  public List<TCPeer> clients() {
    return clients;
  }

  public void remove(Connection connection) {
    String peerName = connection.getPeerName();
    for (TCPeer client : clients) {
      if (client.getKey().equals(peerName)) {
        clients.remove(client);
      }
    }
  }

  public void add(TCPeer peerClient) {
    clients.add(peerClient);
  }
}
