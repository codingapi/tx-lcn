package com.codingapi.txlcn.protocol.message;


/**
 * Interfaces of the messages dispatched between peers in the network
 */
public interface TCMessage extends Message {

  /**
   * receive message handler, msg is this obj.
   *
   * @param connection remote connection
   */
  void handle(Connection connection);


}
