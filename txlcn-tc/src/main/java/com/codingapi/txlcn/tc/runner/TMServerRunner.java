package com.codingapi.txlcn.tc.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.protocol.message.event.OtherTmNodeEvent;
import com.codingapi.txlcn.protocol.message.separate.AbsMessage;
import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.id.SnowflakeStep;
import com.codingapi.txlcn.tc.reporter.TxManagerReporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TMServerRunner {

  private TxConfig txConfig;

  private ProtocolServer protocolServer;

  private ScheduledExecutorService scheduledExecutorService;

  private SnowflakeStep snowFlakeStep;

  private TxManagerReporter txManagerReporter;

  public TMServerRunner(TxConfig txConfig, ProtocolServer protocolServer,
                        SnowflakeStep snowFlakeStep, TxManagerReporter txManagerReporter) {
    this.txConfig = txConfig;
    this.protocolServer = protocolServer;
    this.snowFlakeStep = snowFlakeStep;
    this.txManagerReporter = txManagerReporter;
    this.scheduledExecutorService = Executors.newScheduledThreadPool(1);
  }

  /**
   * 初始化连接
   */
  public void init() {
    try {
      CompletableFuture<Void> futureToNotify = new CompletableFuture<>();
      scheduledExecutorService.scheduleAtFixedRate(() -> {
        txConfig.txManagerAddresses().getINetSocketAddresses().forEach(address -> {
          boolean connectSuccess = protocolServer.connectTo(address.getHostString(), address.getPort(), futureToNotify);
          futureToNotify.whenCompleteAsync((s, throwable) -> {
            log.debug("=> futureToNotify.whenCompleteAsync");
            snowFlakeStep.getGroupIdAndLogId();
          });

          if (connectSuccess) {
            OtherTmNodeEvent otherTmNodeEvent = (OtherTmNodeEvent) txManagerReporter.requestMsg(new OtherTmNodeEvent());
            List<InetSocketAddress> otherNodeList = otherTmNodeEvent.getOtherNodeList();
            txConfig.setINetSocketAddresses(otherNodeList);
          }

        });
      }, 0, 30, TimeUnit.SECONDS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

  }

}
