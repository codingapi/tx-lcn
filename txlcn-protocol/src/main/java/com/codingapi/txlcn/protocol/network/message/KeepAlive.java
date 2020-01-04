package com.codingapi.txlcn.protocol.network.message;

import com.codingapi.txlcn.protocol.Peer;
import com.codingapi.txlcn.protocol.network.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sent to neighbours to notify them about presence of this peer
 */
public class KeepAlive implements Message {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeepAlive.class);

    private static final long serialVersionUID = -4998803925489492616L;

    @Override
    public void handle(Peer peer, Connection connection) {
        LOGGER.debug("Keep alive ping received from {}", connection);
    }

}
