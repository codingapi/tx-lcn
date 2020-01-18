package com.codingapi.txlcn.protocol.client.network.message;

import com.codingapi.txlcn.protocol.message.Connection;
import com.codingapi.txlcn.protocol.message.TCMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * heartbeat message,send with tc client .
 */
@Slf4j
@AllArgsConstructor
public class Heartbeat implements TCMessage {

  private String applicationName;

  @Override
  public void handle(Connection connection) {
    //TM no any message to response.
    if(applicationName!=null){
      connection.setPeerName(applicationName);
    }
    log.info("heart beat message . form {}",connection);
  }
}
