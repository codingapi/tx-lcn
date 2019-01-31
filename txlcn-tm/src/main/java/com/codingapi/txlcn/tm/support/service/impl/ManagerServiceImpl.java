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
package com.codingapi.txlcn.tm.support.service.impl;

import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.tm.core.storage.LockValue;
import com.codingapi.txlcn.txmsg.params.NotifyConnectParams;
import com.codingapi.txlcn.tm.support.service.ManagerService;
import com.codingapi.txlcn.tm.txmsg.MessageCreator;
import com.codingapi.txlcn.txmsg.RpcClient;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/29
 *
 * @author codingapi
 */
@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService {

    private final RpcClient rpcClient;

    private final FastStorage fastStorage;

    private final TxManagerConfig managerConfig;

    @Autowired
    public ManagerServiceImpl(RpcClient rpcClient, FastStorage fastStorage, TxManagerConfig managerConfig) {
        this.rpcClient = rpcClient;
        this.fastStorage = fastStorage;
        this.managerConfig = managerConfig;
    }


    @Override
    public boolean refresh(NotifyConnectParams notifyConnectParams) throws RpcException {
        List<String> keys = rpcClient.loadAllRemoteKey();
        if (keys != null && keys.size() > 0) {
            for (String key : keys) {
                rpcClient.send(key, MessageCreator.newTxManager(notifyConnectParams));
            }
        }
        return true;
    }

    @Override
    public int machineIdSync() throws TxManagerException {
        String contextId = "acquireMachineId";
        Set<String> locks = Sets.newHashSet("machineIdSync");
        LockValue lockValue = new LockValue();
        while (true) {
            try {
                fastStorage.acquireLocks(contextId, locks, lockValue);
                int machineMaxSize = (int) Math.pow(2, managerConfig.getMachineIdLen()) - 1;
                long timeout = managerConfig.getHeartTime() + 2000;
                int id = fastStorage.acquireOrRefreshMachineId(-1, machineMaxSize, timeout);
                log.info("Acquired machine id {}.", id);
                return id;
            } catch (FastStorageException e) {
                if (e.getCode() != FastStorageException.EX_CODE_REPEAT_LOCK) {
                    break;
                }
            } finally {
                try {
                    fastStorage.releaseLocks(contextId, locks);
                } catch (FastStorageException ignored) {
                }
            }
        }
        throw new TxManagerException("non machine id");
    }

    @Override
    public void refreshMachineId(int machineId) throws TxManagerException {
        try {
            int machineMaxSize = (int) Math.pow(2, managerConfig.getMachineIdLen()) - 1;
            long timeout = managerConfig.getHeartTime() + 2000;
            fastStorage.acquireOrRefreshMachineId(machineId, machineMaxSize, timeout);
        } catch (FastStorageException e) {
            throw new TxManagerException(e);
        }
    }
}
