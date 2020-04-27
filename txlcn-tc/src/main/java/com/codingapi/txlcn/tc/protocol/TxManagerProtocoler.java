package com.codingapi.txlcn.tc.protocol;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lorne
 * @date 2020/4/3
 * @description
 */
@AllArgsConstructor
public class TxManagerProtocoler {

    private Protocoler protocoler;

    private List<Connection> connections;

    private Connection leader;

    public TxManagerProtocoler(ProtocolServer protocolServer) {
        this.protocoler =  protocolServer.getProtocoler();
        this.connections = new ArrayList<>(protocoler.getConnections());
        selectLeader();
    }

    private void selectLeader(){
        if(connections.size()>0){
            leader = connections.get(0);
        }
    }


    private void checkLeader(){
        Assert.isNull(leader,"没有可用的连接.");
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMsg(Message message){
        checkLeader();
        leader.send(message);
    }




}
