package com.codingapi.txlcn.tc.runner;

import com.codingapi.txlcn.protocol.ProtocolServer;
import com.codingapi.txlcn.protocol.message.event.OtherTmNodeEvent;
import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.id.SnowflakeStep;
import com.codingapi.txlcn.tc.reporter.TxManagerReporter;
import com.codingapi.txlcn.tc.utils.ListUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wangliang
 */
@Slf4j
public class TmServerRunner {

    private TxConfig txConfig;

    private ProtocolServer protocolServer;

    private ScheduledExecutorService scheduledExecutorService;

    private SnowflakeStep snowFlakeStep;

    private TxManagerReporter reporter;

    public TmServerRunner(TxConfig txConfig, ProtocolServer protocolServer,
                          SnowflakeStep snowFlakeStep, TxManagerReporter txManagerReporter) {
        this.txConfig = txConfig;
        this.protocolServer = protocolServer;
        this.snowFlakeStep = snowFlakeStep;
        this.reporter = txManagerReporter;
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new ThreadFactoryBuilder().setNameFormat("tmServerRunner-pool-%d").build());
    }

    /**
     * 初始化连接
     */
    public void init() {
        try {
            CompletableFuture<Void> futureToNotify = new CompletableFuture<>();
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                List<InetSocketAddress> iNetSocketAddresses = ListUtil.isEmpty(txConfig.getINetSocketAddresses()) ?
                        txConfig.txManagerAddresses() : txConfig.getINetSocketAddresses();
                iNetSocketAddresses.forEach(address -> {
                    protocolServer.connectTo(address.getHostString(), address.getPort(), futureToNotify);
                    futureToNotify.whenCompleteAsync((s, throwable) -> {
                        log.debug("=> futureToNotify.whenCompleteAsync");
                        snowFlakeStep.getGroupIdAndLogId();
                        this.tryToGetMoreTmResource(iNetSocketAddresses);
                    });
                });
            }, 0, 30, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 尝试获得更多 TM 资源
     */
    private void tryToGetMoreTmResource(List<InetSocketAddress> iNetSocketAddresses) {
        if (iNetSocketAddresses.size() < this.txConfig.getTmResource()) {
            log.info("=> Try to get more Tm resource");
            OtherTmNodeEvent requestTmNodeEvent = new OtherTmNodeEvent().setOtherNodeList(iNetSocketAddresses);
            OtherTmNodeEvent responseTmNodeEvent = (OtherTmNodeEvent) reporter.requestMsg(requestTmNodeEvent);
            log.debug("=> tryToGetMoreTmResource.responseTmNodeEvent:{}", responseTmNodeEvent.getOtherNodeList());
            List<InetSocketAddress> otherNodeList = responseTmNodeEvent.getOtherNodeList();
            iNetSocketAddresses.addAll(otherNodeList);
            this.txConfig.setINetSocketAddresses(iNetSocketAddresses);
        }
    }

}
