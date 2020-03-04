package com.codingapi.txlcn.protocol.service;

import com.codingapi.txlcn.protocol.message.Connection;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Slf4j
public class ConnectionService {

    // server name -> connection
    private final Map<String, Connection> connections = new HashMap<String, Connection>();

    public void addConnection(final Connection connection) {
        final String uniqueKey = connection.uniqueKey();
        final Connection previousConnection = connections.put(uniqueKey, connection);

        log.info("Connection to " + uniqueKey + " is added.");

        if (previousConnection != null) {
            previousConnection.close();
            log.warn("Already existing connection to " + uniqueKey + " is closed.");
        }
    }

    public boolean removeConnection(final Connection connection) {
        final boolean removed = connections.remove(connection.uniqueKey()) != null;
        if (removed) {
            log.info(connection + " is removed from connections!");
        } else {
            log.warn("Connection to " + connection.uniqueKey() + " is not removed since not found in connections!");
        }

        return removed;
    }

    public int getNumberOfConnections() {
        return connections.size();
    }

    public Collection<Connection> getConnections() {
        return Collections.unmodifiableCollection(connections.values());
    }


}
