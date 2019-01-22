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

import com.codingapi.txlcn.commons.runner.TxLcnInitializer;
import com.codingapi.txlcn.manager.config.TxManagerConfig;
import com.codingapi.txlcn.manager.core.storage.FastStorage;
import com.codingapi.txlcn.manager.core.storage.FastStorageException;
import com.codingapi.txlcn.manager.cluster.TxManagerAutoCluster;
import com.codingapi.txlcn.manager.support.message.TxLcnManagerServer;
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
public class TMInitialization implements TxLcnInitializer {

    private final TxManagerAutoCluster managerAutoCluster;

    private final FastStorage fastStorage;

    private final TxLcnManagerServer txLcnManagerServer;

    private final TxManagerConfig managerConfig;

    @Autowired
    public TMInitialization(TxManagerAutoCluster managerAutoCluster, FastStorage fastStorage,
                            TxLcnManagerServer txLcnManagerServer, TxManagerConfig managerConfig) {
        this.managerAutoCluster = managerAutoCluster;
        this.fastStorage = fastStorage;
        this.txLcnManagerServer = txLcnManagerServer;
        this.managerConfig = managerConfig;
    }

    @Override
    public void init() throws Exception {

        // init TM RPC Component
        txLcnManagerServer.init();

        // Init TM instance list
        initTMList();

        // auto cluster
        managerAutoCluster.refresh();
    }

    private void initTMList() throws FastStorageException {
        fastStorage.saveTMAddress(managerConfig.getHost() + ":" + managerConfig.getPort());
    }

}
