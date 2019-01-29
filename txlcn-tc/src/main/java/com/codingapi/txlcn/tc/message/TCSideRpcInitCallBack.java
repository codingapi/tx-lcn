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
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import com.codingapi.txlcn.spi.message.listener.ClientInitCallBack;
import com.codingapi.txlcn.spi.message.params.InitClientParams;
import com.codingapi.txlcn.tc.config.TxClientConfig;
import com.codingapi.txlcn.tc.message.helper.MessageCreator;
import com.codingapi.txlcn.tc.support.listener.RpcEnvStatusListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 * @see TMSearcher
 */
@Component
@Slf4j
public class TCSideRpcInitCallBack implements ClientInitCallBack {

    private final RpcClient rpcClient;

    private final TxClientConfig txClientConfig;

    private final String modId;

    private final List<RpcEnvStatusListener> rpcEnvStatusListeners;

    @Autowired
    public TCSideRpcInitCallBack(RpcClient rpcClient, TxClientConfig txClientConfig,
                                 ConfigurableEnvironment environment,
                                 @Autowired(required = false) ServerProperties serverProperties,
                                 List<RpcEnvStatusListener> rpcEnvStatusListeners) {
        this.rpcClient = rpcClient;
        this.txClientConfig = txClientConfig;
        this.modId = ApplicationInformation.modId(environment, serverProperties);
        this.rpcEnvStatusListeners = rpcEnvStatusListeners;
    }

    @Override
    public void connected(String remoteKey) {
        rpcEnvStatusListeners.forEach(rpcEnvStatusListener -> rpcEnvStatusListener.onConnected(remoteKey));
        new Thread(() -> {
            try {
                log.info("Send init message to TM[{}]", remoteKey);
                MessageDto msg = rpcClient.request(remoteKey, MessageCreator.initClient(modId), 5000);
                if (msg.getData() != null) {
                    //每一次建立连接时将会获取最新的时间
                    InitClientParams resParams = msg.loadBean(InitClientParams.class);
                    txClientConfig.applyDtxTime(resParams.getDtxTime());
                    txClientConfig.applyTmRpcTimeout(resParams.getTmRpcTimeout());
                    log.info("Finally, determined dtx time is {}ms. TM rpc timeout is {} ms.",
                            resParams.getDtxTime(), resParams.getTmRpcTimeout());
                    rpcEnvStatusListeners.forEach(rpcEnvStatusListener -> rpcEnvStatusListener.onInitialized(remoteKey));
                    return;
                }
                log.error("TM[{}] exception. connect fail!", remoteKey);
            } catch (RpcException e) {
                log.error("Send init message exception: {}. connect fail!", e.getMessage());
            }
        }).start();
    }

    @Override
    public void connectFail(String remoteKey) {
        rpcEnvStatusListeners.forEach(rpcEnvStatusListener -> rpcEnvStatusListener.onConnectFail(remoteKey));
    }
}
