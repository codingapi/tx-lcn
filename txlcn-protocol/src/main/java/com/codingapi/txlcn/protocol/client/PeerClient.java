package com.codingapi.txlcn.protocol.client;

import com.codingapi.txlcn.protocol.IPeer;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;
import lombok.Data;

@Data
public class PeerClient implements IPeer {

    /**
     * netty connection
     */
    private Connection connection;

    /**
     * TM server host name
     */
    private String host;
    /**
     * TM server port
     */
    private int port;
    /**
     * TM peer name
     */
    private String peerName;

    /**
     * peer primary id
     */
    private String id;


    public PeerClient(String host, int port, String peerName) {
        this.host = host;
        this.port = port;
        this.peerName = peerName;
    }

    public void bindConnection(Connection connection) {
        connection.setPeerName(peerName);
        this.connection = connection;
    }

    public void send(Message msg){
        connection.send(msg);
    }

}
