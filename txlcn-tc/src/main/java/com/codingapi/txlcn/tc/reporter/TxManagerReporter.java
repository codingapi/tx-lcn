package com.codingapi.txlcn.tc.reporter;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;
import com.codingapi.txlcn.protocol.message.separate.SnowflakeMessage;
import com.codingapi.txlcn.protocol.message.separate.TmNodeMessage;
import com.codingapi.txlcn.protocol.message.separate.TransactionMessage;
import com.codingapi.txlcn.tc.config.TxConfig;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lorne
 * @date 2020/4/3
 * @description
 */
@AllArgsConstructor
public class TxManagerReporter {

    private Protocoler protocoler;

    private Collection<Connection> connections;

    private Connection leader;

    private TxConfig txConfig;

    public TxManagerReporter(ProtocolServer protocolServer,TxConfig txConfig) {
        this.protocoler =  protocolServer.getProtocoler();
        this.txConfig = txConfig;
        this.connections = protocoler.getConnections();
    }

    /**
     * 随机选一个 TM 连接
     */
    private void selectLeader() {
        if (connections.size() > 0) {
            List<Connection> connectionList = connections.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            int randomConnection = this.getRandom(0, connectionList.size());
            leader = connectionList.get(randomConnection);
        }
    }


    private void checkLeader(){
        Assert.notNull(leader,"没有可用的TM资源.");
    }

    /**
     * 发送消息
     * @param message message
     */
    public void sendMsg(Message message){
        selectLeader();
        checkLeader();
        leader.send(message);
    }

    /**
     * 请求消息
     * @param message TmNodeMessage
     */
    public TmNodeMessage requestMsg(TmNodeMessage message){
        message.setMessageId(UUID.randomUUID().toString());
        // 第一个接收到 TC 消息的节点
        message.setIsFirstNode(true);
        selectLeader();
        checkLeader();
        return leader.request(message);
    }

    /**
     * 请求消息
     * @param message TransactionMessage
     */
    public TransactionMessage requestMsg(TransactionMessage message){
        message.setModuleName(txConfig.getApplicationName());
        // 第一个接收到 TC 消息的节点
        message.setIsFirstNode(true);
        selectLeader();
        checkLeader();
        return leader.request(message);
    }

    /**
     * 请求消息
     * @param message SnowFlakeMessage
     * @return  SnowflakeMessage
     */
    public SnowflakeMessage requestMsg(SnowflakeMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        // 第一个接收到 TC 消息的节点
        message.setIsFirstNode(true);
        selectLeader();
        checkLeader();
        return leader.request(message);
    }

    /**
     * 取得一个随机数，取值范围是 [min,max-1]
     *
     * @param min 最小数
     * @param max 最大数
     * @return int
     */
    public int getRandom(int min, int max) {
        return new Random().nextInt(max) % (max - min + 1) + min;
    }

}
