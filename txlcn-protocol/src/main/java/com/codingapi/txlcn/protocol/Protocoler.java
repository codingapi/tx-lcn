package com.codingapi.txlcn.protocol;

import com.codingapi.txlcn.protocol.await.Lock;
import com.codingapi.txlcn.protocol.await.LockContext;
import com.codingapi.txlcn.protocol.config.Config;
import com.codingapi.txlcn.protocol.exception.ProtocolException;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;
import com.codingapi.txlcn.protocol.message.separate.TransactionMessage;
import com.codingapi.txlcn.protocol.service.ConnectionService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * @author lorne
 * @date 2020/3/4
 * @description
 */
@Slf4j
public class Protocoler {

    private Channel serverChannel;

    private final Config config;

    private final ConnectionService connectionService;

    private boolean running = true;

    @Override
    public String toString() {
        return "Protocoler{" +
                ", config=" + config +
                ", running=" + running +
                '}';
    }

    public Protocoler(Config config) {
        this.config =config;
        this.connectionService = new ConnectionService();
    }

    public void setBindChannel(Channel serverChannel) {
        this.serverChannel = serverChannel;
        running = true;
    }

    public void handleConnectionOpened(Connection connection) {
        if (isShutdown()) {
            log.warn("New connection of {} ignored since not running", connection);
            return;
        }
        connectionService.addConnection(connection);
    }

    public void handleConnectionClosed(Connection connection) {
        if (connection == null) {
            return;
        }
        connectionService.removeConnection(connection);
    }

    public void leave(final CompletableFuture<Void> futureToNotify) {
        if (isShutdown()) {
            log.warn("server already shut down!");
            futureToNotify.complete(null);
            return;
        }

        Collection<Connection> connections =  getConnections();
        for(Connection connection:connections){
            connection.close();
        }

        serverChannel.closeFuture().addListener(future -> {
            if (future.isSuccess()) {
                futureToNotify.complete(null);
            } else {
                futureToNotify.completeExceptionally(future.cause());
            }
        });
        serverChannel.close();
        running = false;
    }

    public boolean isShutdown() {
        return !running;
    }


    public Collection<Connection> getConnections() {
        return connectionService.getConnections();
    }

    public void sendMsg(String uniqueKey, Message message) {
        Connection connection = connectionService.getConnection(uniqueKey);
        if (connection != null) {
            connection.send(message);
        } else {
            log.warn("This key {} not connected. ", uniqueKey);
        }
    }

    public TransactionMessage requestMsg(String uniqueKey, TransactionMessage message) {
        Connection connection = connectionService.getConnection(uniqueKey);
        if (connection != null) {
            String groupId = message.getGroupId();
            Lock lock =  LockContext.getInstance().addKey(groupId);
            try {
                connection.send(message);
                lock.await(config.getAwaitTime());
                return (TransactionMessage)lock.getRes();
            }finally {
                lock.clear();
            }
        } else {
            log.warn("This key {} not connected. ", uniqueKey);
            throw new ProtocolException("can't send message . ");
        }
    }


    public boolean existConnect(String uniqueKey) {
        return connectionService.existConnect(uniqueKey);
    }
}
