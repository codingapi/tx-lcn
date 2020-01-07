package com.codingapi.txlcn.protocol.manager.network.message.leader;


import com.codingapi.txlcn.protocol.IPeer;
import com.codingapi.txlcn.protocol.manager.Peer;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;

/**
 * Notifies neighbours about announced leader
 */
public class AnnounceLeader implements Message {

  private static final long serialVersionUID = 81362517392480723L;

  private final String leaderName;

  public AnnounceLeader(String leaderName) {
    this.leaderName = leaderName;
  }

  @Override
  public void handle(IPeer peer, Connection connection) {
    peer.optional(Peer.class).ifPresent((p) -> p.handleLeader(connection, leaderName));
  }

}
