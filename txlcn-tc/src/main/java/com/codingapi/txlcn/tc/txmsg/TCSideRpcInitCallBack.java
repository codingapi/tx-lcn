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

import com.codingapi.txlcn.common.util.id.IdGenInit;
import com.codingapi.txlcn.common.util.id.ModIdProvider;
import com.codingapi.txlcn.tc.config.TxClientConfig;
import com.codingapi.txlcn.tc.support.listener.RpcEnvStatusListener;
import com.codingapi.txlcn.txmsg.RpcClient;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.dto.RpcCmd;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import com.codingapi.txlcn.txmsg.listener.ClientInitCallBack;
import com.codingapi.txlcn.txmsg.listener.HeartbeatListener;
import com.codingapi.txlcn.txmsg.params.InitClientParams;
import com.codingapi.txlcn.txmsg.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
public class TCSideRpcInitCallBack implements ClientInitCallBack, HeartbeatListener {

    private final RpcClient rpcClient;

    private final TxClientConfig txClientConfig;

    private final List<RpcEnvStatusListener> rpcEnvStatusListeners;

    private final ModIdProvider modIdProvider;

    private final String applicationName;

    @Autowired
    public TCSideRpcInitCallBack(RpcClient rpcClient, TxClientConfig txClientConfig,
                                 ConfigurableEnvironment environment,
                                 List<RpcEnvStatusListener> rpcEnvStatusListeners, ModIdProvider modIdProvider) {
        this.rpcClient = rpcClient;
        this.txClientConfig = txClientConfig;
        this.rpcEnvStatusListeners = rpcEnvStatusListeners;
        this.modIdProvider = modIdProvider;
        String appName = environment.getProperty("spring.application.name");
        this.applicationName = StringUtils.hasText(appName) ? appName : "application";
    }

    @Override
    public void connected(String remoteKey) {
        rpcEnvStatusListeners.forEach(rpcEnvStatusListener -> rpcEnvStatusListener.onConnected(remoteKey));
        new Thread(() -> {
            try {
                log.info("Send init message to TM[{}]", remoteKey);
                MessageDto msg = rpcClient.request(
                        remoteKey, MessageCreator.initClient(applicationName, modIdProvider.modId()), 5000);
                if (MessageUtils.statusOk(msg)) {
                    //每一次建立连接时将会获取最新的时间
                    InitClientParams resParams = msg.loadBean(InitClientParams.class);
                    // 1. 设置DTX Time 、 TM RPC timeout 和 MachineId
                    txClientConfig.applyDtxTime(resParams.getDtxTime());
                    txClientConfig.applyTmRpcTimeout(resParams.getTmRpcTimeout());
                    txClientConfig.applyMachineId(resParams.getMachineId());

                    // 2. IdGen 初始化
                    IdGenInit.applyDefaultIdGen(resParams.getSeqLen(), resParams.getMachineId());

                    // 3. 日志
                    log.info("Finally, determined dtx time is {}ms, tm rpc timeout is {} ms, machineId is {}",
                            resParams.getDtxTime(), resParams.getTmRpcTimeout(), resParams.getMachineId());
                    // 4. 执行其它监听器
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

    @Override
    public void onTcReceivedHeart(RpcCmd cmd) {
        cmd.getMsg().setData(txClientConfig.getMachineId());
    }
}
