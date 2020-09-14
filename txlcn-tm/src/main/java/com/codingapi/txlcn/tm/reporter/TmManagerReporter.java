package com.codingapi.txlcn.tm.reporter;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;
import com.codingapi.txlcn.protocol.message.separate.AbsMessage;
import com.codingapi.txlcn.tm.config.TmConfig;
import com.codingapi.txlcn.tm.node.TmNode;
import com.codingapi.txlcn.tm.repository.TmNodeRepository;
import com.codingapi.txlcn.tm.util.NetUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.Assert;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author whohim
 */
@AllArgsConstructor
@Data
public class TmManagerReporter {

    private Protocoler protocoler;

    private Collection<Connection> connections;

    private Connection leader;

    private TmConfig tmConfig;

    private TmNodeRepository tmNodeRepository;

    public TmManagerReporter(ProtocolServer protocolServer, TmConfig tmConfig, TmNodeRepository tmNodeRepository) {
        this.protocoler = protocolServer.getProtocoler();
        this.tmConfig = tmConfig;
        this.connections = protocoler.getConnections();
        this.tmNodeRepository = tmNodeRepository;
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

    /**
     * 选择其他 TmNode 节点进行连接
     *
     * @param connection    当前连接 TC
     * @param otherNodeList 其他 TmNode 节点
     */
    private void selectLeaderWithoutTc(Connection connection, List<InetSocketAddress> otherNodeList) {
        if (connections.size() > 0) {
            // 过滤 TC 连接
            List<Connection> connectionList = connections.stream()
                    .filter(connectionBo -> !connectionBo.getUniqueKey().equals(connection.getUniqueKey()))
                    .filter(connectionBo -> otherNodeList.contains(connectionBo.getRemoteAddress()))
                    .collect(Collectors.toList());

            // 获得其他 TM 的连接数
            String hostAddress = Objects.requireNonNull(NetUtil.getLocalhost()).getHostAddress();
            TmNode tmNode = new TmNode()
                    .setId(String.format("%s:%s", hostAddress, tmConfig.getPort()))
                    .setNodeIp(hostAddress)
                    .setPort(tmConfig.getPort())
                    .setTmNodeRepository(tmNodeRepository);

            Map<String, Integer> allTmConnection = tmNode.getAllOtherTmConnection();

            //取 TM 连接数最小的进行连接
            leader = connectionList.stream()
                    .min(Comparator.comparingInt(cnt -> allTmConnection.get(cnt.getUniqueKey())))
                    .orElse(null);
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

    /**
     * TM 间传输消息
     *
     * @param absMessage    AbsMessage
     * @param connection    TC 第一次连接 TM
     * @param otherNodeList 除了头节点 TM 以外的 TM
     * @return AbsMessage
     */
    public AbsMessage requestMsg(AbsMessage absMessage,
                                 Connection connection,
                                 List<InetSocketAddress> otherNodeList) {
        absMessage.setMessageId(UUID.randomUUID().toString());
        // 不是第一个接收到 TC 消息的节点
        absMessage.setIsFirstNode(false);
        selectLeaderWithoutTc(connection, otherNodeList);
        selectLeader();
        checkLeader();
        return leader.request(absMessage);
    }


}
