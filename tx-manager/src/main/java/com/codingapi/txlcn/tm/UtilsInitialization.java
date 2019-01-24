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
package com.codingapi.txlcn.tm;

import com.codingapi.txlcn.commons.exception.FastStorageException;
import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.spi.message.RpcConfig;
import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: TxManger检查
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Component
@Slf4j
public class UtilsInitialization implements TxLcnInitializer {

    private final FastStorage fastStorage;

    private final TxManagerConfig managerConfig;

    private final RpcConfig rpcConfig;

    @Autowired
    public UtilsInitialization(FastStorage fastStorage, TxManagerConfig managerConfig, RpcConfig rpcConfig) {
        this.fastStorage = fastStorage;
        this.managerConfig = managerConfig;
        this.rpcConfig = rpcConfig;
    }

    @Override
    public void init() throws Exception {
        // init rpc env
        rpcEnvInit();

        // Init TM instance list
        initTMList();
    }

    private void initTMList() throws FastStorageException {
        fastStorage.saveTMAddress(managerConfig.getHost() + ":" + managerConfig.getPort());
    }

    private void rpcEnvInit() {
        if (rpcConfig.getWaitTime() == -1) {
            rpcConfig.setWaitTime(5000);
        }
    }
}
