package com.codingapi.txlcn.tm.reporter;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;
import com.codingapi.txlcn.protocol.message.separate.SnowflakeMessage;
import com.codingapi.txlcn.protocol.message.separate.TmNodeMessage;
import com.codingapi.txlcn.protocol.message.separate.TransactionMessage;
import com.codingapi.txlcn.tm.config.TmConfig;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.UUID;

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
        this.protocoler =  protocolServer.getProtocoler();
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
        message.setInstanceId(UUID.randomUUID().toString());
        selectLeader();
        checkLeader();
        return leader.request(message);
    }

    /**
     * 请求消息
     * @param message TransactionMessage
     */
    public TransactionMessage requestMsg(TransactionMessage message){
        message.setModuleName(tmConfig.getName());
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
        message.setInstanceId(UUID.randomUUID().toString());
        selectLeader();
        checkLeader();
        return leader.request(message);
    }



}
