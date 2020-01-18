package com.codingapi.txlcn.protocol.manager.network.message.leader;

import com.codingapi.txlcn.protocol.manager.TMPeer;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.TMMessage;

/**
 * Notifies the peer ,which started the election, that this peer rejected its election
 */
public class Rejection implements TMMessage {

  private static final long serialVersionUID = -4458007227538796558L;

  @Override
  public void handle(TMPeer peer, Connection connection) {
    peer.handleRejection(connection);
  }

}
