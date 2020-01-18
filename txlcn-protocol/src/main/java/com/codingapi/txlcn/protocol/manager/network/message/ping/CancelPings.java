package com.codingapi.txlcn.protocol.manager.network.message.ping;

import com.codingapi.txlcn.protocol.manager.TMPeer;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.TMMessage;

public class CancelPings implements TMMessage {

  private static final long serialVersionUID = -8650899535821394626L;

  private String peerName;

  public CancelPings(String peerName) {
    this.peerName = peerName;
  }

  @Override
  public void handle(TMPeer peer, Connection connection) {
    peer.cancelPings(connection, peerName);
  }

  @Override
  public String toString() {
    return "RemovePings{" +
        "peerName='" + peerName + '\'' +
        '}';
  }

}
