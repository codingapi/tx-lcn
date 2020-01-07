package com.codingapi.txlcn.protocol.message;


import com.codingapi.txlcn.protocol.IPeer;
import java.io.Serializable;

/**
 * Interfaces of the messages dispatched between peers in the network
 */
public interface Message extends Serializable {

  /**
   * receive message handler, msg is this obj.
   *
   * @param peer self peer
   * @param connection remote connection
   */
  void handle(IPeer peer, Connection connection);


}
