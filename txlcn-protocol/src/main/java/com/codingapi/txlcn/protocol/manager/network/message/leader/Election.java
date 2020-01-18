package com.codingapi.txlcn.protocol.manager.network.message.leader;


import com.codingapi.txlcn.protocol.manager.TMPeer;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.TMMessage;

/**
 * Notifies other peers about the election started by this peer
 */
public class Election implements TMMessage {

  private static final long serialVersionUID = 3025595002500496571L;

  @Override
  public void handle(TMPeer peer, Connection connection) {
    peer.handleElection(connection);
  }

}
