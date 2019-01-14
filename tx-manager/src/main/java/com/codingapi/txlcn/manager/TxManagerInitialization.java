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
package com.codingapi.txlcn.manager;

import com.codingapi.txlcn.manager.config.TxManagerConfig;
import com.codingapi.txlcn.spi.message.RpcServerInitializer;
import com.codingapi.txlcn.spi.message.dto.ManagerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/11/29
 *
 * @author lorne
 */
@Component
public class TxManagerInitialization {

    private final TxManagerConfig txManagerConfig;

    private final RpcServerInitializer rpcServerInitializer;

    @Autowired
    public TxManagerInitialization(TxManagerConfig txManagerConfig, RpcServerInitializer rpcServerInitializer) {
        this.txManagerConfig = txManagerConfig;
        this.rpcServerInitializer = rpcServerInitializer;
    }

    @PostConstruct
    public void start(){
        ManagerProperties managerProperties = new ManagerProperties();
        managerProperties.setCheckTime(txManagerConfig.getHeartTime());
        managerProperties.setRpcPort(txManagerConfig.getPort());
        rpcServerInitializer.init(managerProperties);
    }
}
