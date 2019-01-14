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
package com.codingapi.txlcn.client.message.helper;

import com.codingapi.txlcn.spi.message.params.TxExceptionParams;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/29
 *
 * @author ujued
 */
@Component
@Slf4j
public class TxMangerReporter {

    private final RpcClient rpcClient;

    @Autowired
    public TxMangerReporter(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    /**
     * Manager 记录事务状态
     *
     * @param groupId groupId
     * @param unitId unitId
     * @param registrar registrar
     * @param state state
     */
    public void reportTransactionState(String groupId, String unitId, Short registrar, int state) {
        TxExceptionParams txExceptionParams = new TxExceptionParams();
        txExceptionParams.setGroupId(groupId);
        txExceptionParams.setRegistrar(registrar);
        txExceptionParams.setTransactionState((short) state);
        txExceptionParams.setUnitId(unitId);
        report(txExceptionParams);
    }

    /**
     * Manager 记录TXC回滚失败
     *
     * @param groupId groupId
     * @param unitId unitId
     */
    public void reportTxcRollbackException(String groupId, String unitId) {
        TxExceptionParams txExceptionParams = new TxExceptionParams();
        txExceptionParams.setGroupId(groupId);
        txExceptionParams.setRegistrar(TxExceptionParams.TXC_ROLLBACK_ERROR);
        txExceptionParams.setTransactionState((short) 0);
        txExceptionParams.setUnitId(unitId);
        report(txExceptionParams);
    }

    private void report(TxExceptionParams txExceptionParams) {
        while (true) {
            try {
                rpcClient.send(rpcClient.loadRemoteKey(), MessageCreator.writeTxException(txExceptionParams));
                break;
            } catch (RpcException e) {
                if (e.getCode() == RpcException.NON_TX_MANAGER) {
                    log.error("report transaction state error. non tx-manager is alive.");
                    break;
                }
            }
        }
    }
}
