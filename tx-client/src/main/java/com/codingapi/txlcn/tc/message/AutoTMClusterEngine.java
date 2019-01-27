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
package com.codingapi.txlcn.tc.message;

import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.tc.config.TxClientConfig;
import com.codingapi.txlcn.tc.support.listener.RpcEnvStatusListener;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * Date: 1/27/19
 *
 * @author ujued
 */
@Component
@Slf4j
public class AutoTMClusterEngine implements RpcEnvStatusListener {

    private AtomicInteger initCount = new AtomicInteger(0);

    private final TxClientConfig txClientConfig;

    private final ReliableMessenger reliableMessenger;

    @Autowired
    public AutoTMClusterEngine(TxClientConfig txClientConfig, ReliableMessenger reliableMessenger) {
        this.txClientConfig = txClientConfig;
        this.reliableMessenger = reliableMessenger;
    }

    @Override
    public void onConnected(String remoteKey) {

    }

    @Override
    public void onInitialized(String remoteKey) {
        prepareToResearchTMCluster();
    }

    @Override
    public void onConnectFail(String remoteKey) {
        try {
            reliableMessenger.reportInvalidTM(Sets.newHashSet(remoteKey));
        } catch (RpcException e) {
            log.error("{} on reportInvalidTM.", e.getMessage());
        }
        prepareToResearchTMCluster();
    }


    private void prepareToResearchTMCluster() {
        int count = initCount.incrementAndGet();
        int size = txClientConfig.getManagerAddress().size();
        if (count == size) {
            TMSearcher.search();
        } else if (count > size) {
            TMSearcher.searchedOne();
        }
    }
}
