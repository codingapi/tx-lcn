package com.codingapi.txlcn.tc.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tc.config.TxConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TMServerRunner {

  private TxConfig txConfig;

  private ProtocolServer protocolServer;

  private ScheduledExecutorService scheduledExecutorService;

  public TMServerRunner(TxConfig txConfig, ProtocolServer protocolServer) {
    this.txConfig = txConfig;
    this.protocolServer = protocolServer;
    this. scheduledExecutorService = Executors.newScheduledThreadPool(1);
  }


  /**
   * 初始化连接
   */
  public void init() {
    scheduledExecutorService.scheduleAtFixedRate(()->{
      txConfig.txManagerAddresses().forEach(address->{
        protocolServer.connectTo(address.getHostString(),address.getPort());
      });
    },0,30, TimeUnit.SECONDS);
  }

}
