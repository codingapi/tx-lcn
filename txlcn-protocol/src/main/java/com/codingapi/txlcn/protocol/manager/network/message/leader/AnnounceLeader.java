package com.codingapi.txlcn.protocol.manager.network.message.leader;


import com.codingapi.txlcn.protocol.manager.TMPeer;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.TMMessage;

/**
 * Notifies neighbours about announced leader
 */
public class AnnounceLeader implements TMMessage {

  private static final long serialVersionUID = 81362517392480723L;

  private final String leaderName;

  public AnnounceLeader(String leaderName) {
    this.leaderName = leaderName;
  }

  @Override
  public void handle(TMPeer peer, Connection connection) {
    peer.handleLeader(connection, leaderName);
  }

}
