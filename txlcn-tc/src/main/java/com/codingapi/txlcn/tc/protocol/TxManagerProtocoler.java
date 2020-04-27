package com.codingapi.txlcn.tc.protocol;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.protocol.Protocoler;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author lorne
 * @date 2020/4/3
 * @description
 */
@AllArgsConstructor
public class TxManagerProtocoler {

    private Protocoler protocoler;

    private Collection<Connection> connections;

    private Connection leader;

    public TxManagerProtocoler(ProtocolServer protocolServer) {
        this.protocoler =  protocolServer.getProtocoler();
        this.connections = protocoler.getConnections();
    }

    private void selectLeader(){
        if(connections.size()>0){
            Iterator<Connection> iterator = connections.iterator();
            while (iterator.hasNext()) {
                leader = iterator.next();
                if(leader!=null){
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
     * @param message
     */
    public void sendMsg(Message message){
        selectLeader();
        checkLeader();
        leader.send(message);
    }




}
