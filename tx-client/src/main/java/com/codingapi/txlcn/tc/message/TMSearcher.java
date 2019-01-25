package com.codingapi.txlcn.tc.message;

import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.commons.runner.TxlcnRunnerOrders;
import com.codingapi.txlcn.commons.util.ApplicationInformation;
import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.spi.message.RpcClientInitializer;
import com.codingapi.txlcn.spi.message.dto.TxManagerHost;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Date: 19-1-23 下午5:54
 *
 * @author ujued
 */
@Component
@Slf4j
public class TMSearcher implements TxLcnInitializer {

    private static RpcClientInitializer RPC_CLIENT_INITIALIZER;

    private static ReliableMessenger RELIABLE_MESSENGER;

    private final RpcClientInitializer rpcClientInitializer;

    private final ReliableMessenger reliableMessenger;

    @Autowired
    private ConfigurableEnvironment environment;

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    public TMSearcher(RpcClientInitializer rpcClientInitializer, ReliableMessenger reliableMessenger) {
        this.rpcClientInitializer = rpcClientInitializer;
        this.reliableMessenger = reliableMessenger;
    }

    @Override
    public void init() {
        // 1. util class init
        Transactions.setApplicationIdWhenRunning(ApplicationInformation.modId(environment, serverProperties));

        // 2. TMSearcher init
        RPC_CLIENT_INITIALIZER = rpcClientInitializer;
        RELIABLE_MESSENGER = reliableMessenger;
    }

    @Override
    public int order() {
        return TxlcnRunnerOrders.MAX;
    }

    /**
     * 重新搜寻TM
     */
    public static void search() {
        Objects.requireNonNull(RPC_CLIENT_INITIALIZER);
        log.info("Searching for more TM...");
        try {
            HashSet<String> cluster = RELIABLE_MESSENGER.queryTMCluster();
            if (cluster.isEmpty()) {
                log.info("No more TM.");
                echoSuccessful();
                return;
            }
            CountDownLatch clusterCountLatch = new CountDownLatch(cluster.size());
            RPC_CLIENT_INITIALIZER.init(TxManagerHost.parserList(new ArrayList<>(cluster)), clusterCountLatch);
            clusterCountLatch.await(10, TimeUnit.SECONDS);
            echoSuccessful();
        } catch (RpcException | InterruptedException e) {
            throw new IllegalStateException("There is no normal TM.");
        }
    }

    private static void echoSuccessful() {
        log.info("TC[{}] established TM Cluster successfully!", Transactions.APPLICATION_ID_WHEN_RUNNING);
        log.info("TM cluster's size: {}", RELIABLE_MESSENGER.clusterSize());
    }
}
