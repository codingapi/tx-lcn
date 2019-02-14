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

import com.codingapi.txlcn.common.exception.FastStorageException;
import com.codingapi.txlcn.common.exception.TxManagerException;
import com.codingapi.txlcn.common.util.ApplicationInformation;
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.tm.txmsg.RpcExecuteService;
import com.codingapi.txlcn.tm.txmsg.TransactionCmd;
import com.codingapi.txlcn.txmsg.LCNCmdType;
import com.codingapi.txlcn.txmsg.dto.RpcCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Description:
 * Date: 19-1-25 上午11:11
 *
 * @author ujued
 */
@Component("rpc_clean-invalid-tm")
@Slf4j
public class CleanInvalidTMExecuteService implements RpcExecuteService {

    private final FastStorage fastStorage;

    private static final TxLogger txLogger = TxLogger.newLogger(CleanInvalidTMExecuteService.class);

    @Autowired
    public CleanInvalidTMExecuteService(FastStorage fastStorage) {
        this.fastStorage = fastStorage;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxManagerException {
        HashSet hashSet = transactionCmd.getMsg().loadBean(HashSet.class);
        for (Object address : hashSet) {
            String[] args = ApplicationInformation.splitAddress(address.toString());
            try {
                fastStorage.removeTMProperties(args[0], Integer.valueOf(args[1]));
            } catch (FastStorageException e) {
                txLogger.error(LCNCmdType.cleanInvalidTM.getName(), "Remove TM {} fail.", address);
            }
        }
        log.info("Clean invalid TM: {}", hashSet);
        return null;
    }
}
