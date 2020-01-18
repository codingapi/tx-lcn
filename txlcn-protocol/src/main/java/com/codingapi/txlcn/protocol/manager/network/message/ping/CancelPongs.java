package com.codingapi.txlcn.protocol.manager.network.message.ping;

import com.codingapi.txlcn.protocol.manager.TMPeer;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.TMMessage;

public class CancelPongs implements TMMessage {

  private static final long serialVersionUID = 5147827390577329607L;

  private final String peerName;

  public CancelPongs(String peerName) {
    this.peerName = peerName;
  }

  @Override
  public void handle(TMPeer peer, Connection connection) {
    peer.cancelPongs(peerName);
  }

  @Override
  public String toString() {
    return "RemovePongs{" +
        "peerName='" + peerName + '\'' +
        '}';
  }

}
