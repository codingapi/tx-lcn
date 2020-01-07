package com.codingapi.txlcn.protocol.client;

import com.codingapi.txlcn.protocol.IPeer;
import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.Message;
import java.util.UUID;
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
   * netty primary key
   */
  private String key;

  /**
   * application name
   */
  private String applicationName;


  public PeerClient(String applicationName, String host, int port) {
    this.applicationName = applicationName;
    this.host = host;
    this.port = port;
    this.key = UUID.randomUUID().toString();
  }

  public void bindConnection(Connection connection) {
    connection.setPeerName(key);
    this.connection = connection;
  }

  public void send(Message msg) {
    connection.send(msg);
  }

  @Override
  public String toString() {
    return "PeerClient{" +
        "host='" + host + '\'' +
        ", port=" + port +
        ", key='" + key + '\'' +
        ", applicationName='" + applicationName + '\'' +
        '}';
  }

  public void close() {
    connection.close();
  }
}
