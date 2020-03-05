package com.codingapi.txlcn.tm.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtocolRunner {

  private final ProtocolServer protocolServer;

  public ProtocolRunner(ProtocolServer protocolServer) {
    this.protocolServer = protocolServer;
  }

  public void start() {
    try {
      protocolServer.start();
    } catch (InterruptedException e) {
      log.error("peer start error.", e);
      throw new RuntimeException(e);
    }
  }

}
