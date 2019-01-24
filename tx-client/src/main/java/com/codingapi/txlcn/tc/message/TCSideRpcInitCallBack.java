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

import com.codingapi.txlcn.commons.util.ApplicationInformation;
import com.codingapi.txlcn.spi.message.listener.ClientInitCallBack;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.params.InitClientParams;
import com.codingapi.txlcn.tc.config.TxClientConfig;
import com.codingapi.txlcn.tc.message.helper.MessageCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Component
@Slf4j
public class TCSideRpcInitCallBack implements ClientInitCallBack {

    private final RpcClient rpcClient;

    private final TxClientConfig txClientConfig;

    private final String modId;

    private CountDownLatch clusterCountLatch;

    @Autowired
    public TCSideRpcInitCallBack(RpcClient rpcClient, TxClientConfig txClientConfig, ConfigurableEnvironment environment,
                                 @Autowired(required = false) ServerProperties serverProperties) {
        this.rpcClient = rpcClient;
        this.txClientConfig = txClientConfig;
        this.clusterCountLatch = new CountDownLatch(txClientConfig.getManagerAddress().size());
        this.modId = ApplicationInformation.modId(environment, serverProperties);

        new Thread(() -> {
            try {
                clusterCountLatch.await(20, TimeUnit.SECONDS);
                log.info("TC[{}] established TM Cluster successfully!", modId);
                TMSearcher.search();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }).start();
    }

    @Override
    public void connected(String remoteKey) {
        new Thread(() -> {
            try {
                log.info("Send init message to TM[{}]", remoteKey);
                MessageDto msg = rpcClient.request(remoteKey, MessageCreator.initClient(modId));
                if (msg.getData() != null) {
                    //每一次建立连接时将会获取最新的时间
                    InitClientParams resParams = msg.loadBean(InitClientParams.class);
                    long dtxTime = resParams.getDtxTime();
                    txClientConfig.setDtxTime(dtxTime);
                    log.info("Finally, determined dtx time is {}ms.", dtxTime);
                    clusterCountLatch.countDown();
                    return;
                }
                log.error("TM[{}] exception. connect fail!", remoteKey);
            } catch (RpcException e) {
                log.error("Send init message exception: {}. connect fail!", e.getMessage());
            }
        }).start();
    }

    @Override
    public void disconnected(String remoteKey) {
        // nothing to do
    }
}
