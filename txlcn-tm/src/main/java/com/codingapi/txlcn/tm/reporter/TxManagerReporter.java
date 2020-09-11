package com.codingapi.txlcn.tm.reporter;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;
import com.codingapi.txlcn.protocol.message.separate.SnowflakeMessage;
import com.codingapi.txlcn.protocol.message.separate.TmNodeMessage;
import com.codingapi.txlcn.tm.config.TmConfig;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author whohim
 */
@AllArgsConstructor
public class TxManagerReporter {

    private Protocoler protocoler;

    private Collection<Connection> connections;

    private Connection leader;

    private TmConfig tmConfig;

    public TxManagerReporter(ProtocolServer protocolServer, TmConfig tmConfig) {
        this.protocoler = protocolServer.getProtocoler();
        this.tmConfig = tmConfig;
        this.connections = protocoler.getConnections();
    }

    private void selectLeader() {
        if (connections.size() > 0) {
            for (Connection connection : connections) {
                leader = connection;
                if (leader != null) {
                    break;
                }

            }
        }
    }

    private void selectLeaderWithoutTc(Connection connection, List<InetSocketAddress> otherNodeList) {
        if (connections.size() > 0) {
            List<Connection> connectionList = connections.stream()
                    .filter(connectionBo -> !connectionBo.getUniqueKey().equals(connection.getUniqueKey()))
                    .filter(connectionBo -> otherNodeList.contains(connectionBo.getRemoteAddress()))
                    .collect(Collectors.toList());

            //todo 负载均衡算法
            for (Connection cnt : connectionList) {
                leader = cnt;
                if (leader != null) {
                    break;
                }
            }
        }
    }


    private void checkLeader() {
        Assert.notNull(leader, "没有可用的TM资源.");
    }

    /**
     * 发送消息
     *
     * @param message message
     */
    public void sendMsg(Message message) {
        selectLeader();
        checkLeader();
        leader.send(message);
    }

//    public AbsMessage requestMsg(AbsMessage absMessage) {
//        absMessage.setMessageId(UUID.randomUUID().toString());
//        // 第一个接收到 TC 消息的节点
//        absMessage.setIsFirstNode(true);
//        selectLeader();
//        checkLeader();
//        return leader.request(absMessage);
//    }

    /**
     * 请求消息
     *
     * @param message TmNodeMessage
     */
    public TmNodeMessage requestMsg(TmNodeMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        // 第一个接收到 TC 消息的节点
        message.setIsFirstNode(true);
        selectLeader();
        checkLeader();
        return leader.request(message);
    }


    /**
     * 请求消息
     *
     * @param message       SnowFlakeMessage
     * @param connection  TC 第一次连接 TM
     * @param otherNodeList 除了头节点 TM 以外的 TM
     * @return SnowflakeMessage
     */
    public SnowflakeMessage requestMsg(SnowflakeMessage message, Connection connection, List<InetSocketAddress> otherNodeList) {
        message.setMessageId(UUID.randomUUID().toString());
        // 第一个接收到 TC 消息的节点
        message.setIsFirstNode(false);
        selectLeaderWithoutTc(connection, otherNodeList);
        checkLeader();
        return leader.request(message);
    }


}
