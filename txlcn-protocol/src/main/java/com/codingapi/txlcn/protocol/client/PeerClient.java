package com.codingapi.txlcn.protocol.client;

import com.codingapi.txlcn.protocol.IPeer;
import lombok.Data;

@Data
public class PeerClient implements IPeer {

    public PeerClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * TM server host name
     */
    private String host;
    /**
     * TM server port
     */
    private int port;
    /**
     * self application name
     */
    private String applicationName;

    /**
     * peer primary id
     */
    private String id;

}
