package com.codingapi.txlcn.tc.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.id.SnowFlakeStep;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TMServerRunner {

  private TxConfig txConfig;

  private ProtocolServer protocolServer;

  private ScheduledExecutorService scheduledExecutorService;

  private SnowFlakeStep snowFlakeStep;

  public TMServerRunner(TxConfig txConfig, ProtocolServer protocolServer, SnowFlakeStep snowFlakeStep) {
    this.txConfig = txConfig;
    this.protocolServer = protocolServer;
    this.snowFlakeStep = snowFlakeStep;
    this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
  }


  /**
   * 初始化连接
   */
  public void init() {
    try {
      CompletableFuture<Void> futureToNotify = new CompletableFuture<>();
      scheduledExecutorService.scheduleAtFixedRate(() -> {
        txConfig.txManagerAddresses().forEach(address -> {
          protocolServer.connectTo(address.getHostString(), address.getPort(), futureToNotify);
          futureToNotify.whenCompleteAsync((s, throwable) -> {
            log.debug("=> futureToNotify.whenCompleteAsync");
            snowFlakeStep.getGroupIdAndLogId();
          });
        });
      }, 0, 30, TimeUnit.SECONDS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

  }

}
