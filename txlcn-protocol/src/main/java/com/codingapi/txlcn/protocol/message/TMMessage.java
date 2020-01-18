package com.codingapi.txlcn.protocol.message;

import com.codingapi.txlcn.protocol.manager.TMPeer;

public interface TMMessage extends Message {

  /**
   * receive message handler, msg is this obj.
   *
   * @param peer self peer
   * @param connection remote connection
   */
  void handle(TMPeer peer, Connection connection);


}
