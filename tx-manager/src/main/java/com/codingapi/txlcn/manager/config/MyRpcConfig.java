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
package com.codingapi.txlcn.manager.config;

import com.codingapi.txlcn.spi.message.RpcConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Date: 19-1-9 下午6:13
 *
 * @author ujued
 */
@Configuration
public class MyRpcConfig implements InitializingBean {

    private final RpcConfig rpcConfig;

    private final TxManagerConfig managerConfig;

    @Autowired
    public MyRpcConfig(RpcConfig rpcConfig, TxManagerConfig managerConfig) {
        this.rpcConfig = rpcConfig;
        this.managerConfig = managerConfig;
    }

    @Override
    public void afterPropertiesSet() {
        rpcConfig.setAttrDelayTime(managerConfig.getDtxTime() / 1000);
    }
}
