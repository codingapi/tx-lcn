package com.codingapi.txlcn.protocol.manager.network.message;

import com.codingapi.txlcn.protocol.manager.TMPeer;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.TMMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sent to neighbours to notify them about presence of this peer
 */
public class KeepAlive implements TMMessage {

  private static final Logger LOGGER = LoggerFactory.getLogger(KeepAlive.class);

  private static final long serialVersionUID = -4998803925489492616L;

  @Override
  public void handle(TMPeer peer, Connection connection) {
    LOGGER.debug("Keep alive ping received from {}", connection);
  }

}
