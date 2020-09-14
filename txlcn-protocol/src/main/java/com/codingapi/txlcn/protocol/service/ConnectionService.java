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
    private final Map<String, Connection> connections = new HashMap<>();

    public void addConnection(final Connection connection) {
        final String uniqueKey = connection.getUniqueKey();
        final Connection previousConnection = connections.get(uniqueKey);
        if (previousConnection != null) {
            connection.close();
            log.warn("Already existing connection to " + uniqueKey + " is closed.");
        }else{
            connections.put(uniqueKey,connection);
            log.info("Connection to " + uniqueKey + " is added.");
        }
    }

    public boolean removeConnection(final Connection connection) {
        final boolean removed = connections.remove(connection.getUniqueKey()) != null;
        if (removed) {
            log.info(connection + " is removed from connections!");
        } else {
            log.warn("Connection to " + connection.getUniqueKey() + " is not removed since not found in connections!");
        }

        return removed;
    }

    public int getNumberOfConnections() {
        return connections.size();
    }

    public Collection<Connection> getConnections() {
        return Collections.unmodifiableCollection(connections.values());
    }

    public Connection getConnection(String uniqueKey) {
        return connections.get(uniqueKey);
    }

    public boolean existConnect(String uniqueKey) {
        return connections.get(uniqueKey)!=null;
    }
}
