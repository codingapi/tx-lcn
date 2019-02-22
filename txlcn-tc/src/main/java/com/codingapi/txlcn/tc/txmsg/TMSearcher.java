/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.tc.txmsg;

import com.codingapi.txlcn.common.util.ApplicationInformation;
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.common.util.id.ModIdProvider;
import com.codingapi.txlcn.txmsg.RpcClientInitializer;
import com.codingapi.txlcn.txmsg.dto.TxManagerHost;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import com.codingapi.txlcn.tc.config.TxClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

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
public class TMSearcher {

    private static RpcClientInitializer RPC_CLIENT_INITIALIZER;

    private static ReliableMessenger RELIABLE_MESSENGER;

    private static volatile CountDownLatch clusterCountLatch;

    private static int knownTMClusterSize = 1;

    @Autowired
    public TMSearcher(RpcClientInitializer rpcClientInitializer, TxClientConfig clientConfig,
                      ReliableMessenger reliableMessenger, ModIdProvider modIdProvider) {
        // 1. util class init
        Transactions.setApplicationIdWhenRunning(modIdProvider.modId());

        // 2. TMSearcher init
        RPC_CLIENT_INITIALIZER = rpcClientInitializer;
        RELIABLE_MESSENGER = reliableMessenger;
        knownTMClusterSize = clientConfig.getManagerAddress().size();
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
                echoTMClusterSuccessful();
                return;
            }
            clusterCountLatch = new CountDownLatch(cluster.size() - knownTMClusterSize);
            log.debug("wait connect size is {}", cluster.size() - knownTMClusterSize);
            RPC_CLIENT_INITIALIZER.init(TxManagerHost.parserList(new ArrayList<>(cluster)), true);
            clusterCountLatch.await(10, TimeUnit.SECONDS);
            echoTMClusterSuccessful();
        } catch (RpcException | InterruptedException e) {
            throw new IllegalStateException("There is no normal TM.");
        }
    }

    /**
     * 搜索到一个
     * @return is searched one
     */
    public static boolean searchedOne() {
        if (Objects.nonNull(clusterCountLatch)) {
            if (clusterCountLatch.getCount() == 0) {
                return false;
            }
            clusterCountLatch.countDown();
            return true;
        }
        return false;
    }

    private static void echoTMClusterSuccessful() {
        log.info("TC[{}] established TM cluster successfully!", Transactions.APPLICATION_ID_WHEN_RUNNING);
        echoTmClusterSize();
    }

    public static void echoTmClusterSize() {
        log.info("TM cluster's size: {}", RELIABLE_MESSENGER.clusterSize());
    }
}
