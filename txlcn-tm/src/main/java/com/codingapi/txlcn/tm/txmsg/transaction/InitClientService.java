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
package com.codingapi.txlcn.tm.txmsg.transaction;

import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.common.util.ApplicationInformation;
import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.support.service.ManagerService;
import com.codingapi.txlcn.tm.txmsg.RpcExecuteService;
import com.codingapi.txlcn.tm.txmsg.TransactionCmd;
import com.codingapi.txlcn.txmsg.RpcClient;
import com.codingapi.txlcn.txmsg.RpcConfig;
import com.codingapi.txlcn.txmsg.params.InitClientParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Service(value = "rpc_init-client")
@Slf4j
public class InitClientService implements RpcExecuteService {

    private final RpcClient rpcClient;

    private final TxManagerConfig txManagerConfig;

    private final ConfigurableEnvironment environment;

    private final ServerProperties serverProperties;

    private final RpcConfig rpcConfig;

    private final ManagerService managerService;

    @Autowired
    public InitClientService(RpcClient rpcClient, TxManagerConfig txManagerConfig, ConfigurableEnvironment environment,
                             @Autowired(required = false) ServerProperties serverProperties, RpcConfig rpcConfig,
                             ManagerService managerService) {
        this.rpcClient = rpcClient;
        this.txManagerConfig = txManagerConfig;
        this.environment = environment;
        this.serverProperties = serverProperties;
        this.rpcConfig = rpcConfig;
        this.managerService = managerService;
    }


    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxManagerException {
        log.info("init client - >{}", transactionCmd);
        InitClientParams initClientParams = transactionCmd.getMsg().loadBean(InitClientParams.class);
        rpcClient.bindAppName(transactionCmd.getRemoteKey(), initClientParams.getAppName());
        // Machine len and id
        initClientParams.setSeqLen(txManagerConfig.getSeqLen());
        initClientParams.setMachineId(managerService.machineIdSync());
        // DTX Time and TM timeout.
        initClientParams.setDtxTime(txManagerConfig.getDtxTime());
        initClientParams.setTmRpcTimeout(rpcConfig.getWaitTime());
        // TM Name
        initClientParams.setAppName(ApplicationInformation.modId(environment, serverProperties));
        return initClientParams;
    }
}
