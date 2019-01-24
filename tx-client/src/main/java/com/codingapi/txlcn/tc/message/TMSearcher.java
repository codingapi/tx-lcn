package com.codingapi.txlcn.tc.message;

import com.codingapi.txlcn.spi.message.RpcClientInitializer;
import com.codingapi.txlcn.spi.message.dto.TxManagerHost;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class TMSearcher {

    private static RpcClientInitializer RPC_CLIENT_INITIALIZER;

    private static ReliableMessenger RELIABLE_MESSENGER;

    public static void init(RpcClientInitializer rpcClientInitializer, ReliableMessenger reliableMessenger) {
        RPC_CLIENT_INITIALIZER = rpcClientInitializer;
        RELIABLE_MESSENGER = reliableMessenger;
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
                return;
            }
            CountDownLatch clusterCountLatch = new CountDownLatch(cluster.size());
            RPC_CLIENT_INITIALIZER.init(TxManagerHost.parserList(new ArrayList<>(cluster)), clusterCountLatch);
            clusterCountLatch.await(10, TimeUnit.SECONDS);
            log.info("TM cluster's size: {}", RELIABLE_MESSENGER.clusterSize());
        } catch (RpcException | InterruptedException e) {
            throw new IllegalStateException("There is no normal TM.");
        }
    }
}
