package com.codingapi.txlcn.tm.runner;

import com.codingapi.txlcn.protocol.manager.PeerHandle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtocolRunner {

  private final PeerHandle peerHandle;

  public ProtocolRunner(PeerHandle peerHandle) {
    this.peerHandle = peerHandle;
  }

  public void start() {
    try {
      peerHandle.start();
    } catch (InterruptedException e) {
      log.error("peer start error.", e);
      throw new RuntimeException(e);
    }
  }
}
