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
import com.codingapi.txlcn.txmsg.params.DTXLockParams;
import com.codingapi.txlcn.tm.txmsg.RpcExecuteService;
import com.codingapi.txlcn.tm.txmsg.TransactionCmd;
import com.codingapi.txlcn.tm.core.storage.FastStorage;
import com.codingapi.txlcn.common.exception.FastStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Description:
 * Date: 19-1-22 下午3:02
 *
 * @author ujued
 */
@Component("rpc_release-dtx-lock")
public class ReleaseDTXLockExecuteService implements RpcExecuteService {

    private final FastStorage fastStorage;

    @Autowired
    public ReleaseDTXLockExecuteService(FastStorage fastStorage) {
        this.fastStorage = fastStorage;
    }

    @Override
    public Serializable execute(TransactionCmd transactionCmd) throws TxManagerException {
        DTXLockParams dtxLockParams = transactionCmd.getMsg().loadBean(DTXLockParams.class);
        try {
            fastStorage.releaseLocks(dtxLockParams.getContextId(), dtxLockParams.getLocks());
            return true;
        } catch (FastStorageException e) {
            throw new TxManagerException(e);
        }
    }
}
