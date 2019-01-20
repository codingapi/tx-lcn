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

import com.codingapi.txlcn.spi.message.dto.MessageDto;
import com.codingapi.txlcn.spi.message.params.TxExceptionParams;
import com.codingapi.txlcn.spi.message.RpcClient;
import com.codingapi.txlcn.spi.message.exception.RpcException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: 客户端上报Manager
 * Date: 2018/12/29
 *
 * @author ujued
 */
@Component
@Slf4j
public class TxMangerReporter {

    private final RpcClient rpcClient;

    private static final String REPORT_ERROR_MESSAGE = "report transaction transactionState error";

    @Autowired
    public TxMangerReporter(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    /**
     * Manager 记录事务状态
     *
     * @param groupId   groupId
     * @param unitId    unitId
     * @param registrar registrar
     * @param state     transactionState
     */
    public void reportTransactionState(String groupId, String unitId, Short registrar, int state) {
        TxExceptionParams txExceptionParams = new TxExceptionParams();
        txExceptionParams.setGroupId(groupId);
        txExceptionParams.setRegistrar(registrar);
        txExceptionParams.setTransactionState(state);
        txExceptionParams.setUnitId(unitId);
        report(txExceptionParams);
    }

    /**
     * Manager 记录TCC清理事务失败
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @param state state
     */
    public void reportTccCleanException(String groupId, String unitId, int state) {
        TxExceptionParams txExceptionParams = new TxExceptionParams();
        txExceptionParams.setGroupId(groupId);
        txExceptionParams.setRegistrar(TxExceptionParams.TCC_CLEAN_ERROR);
        txExceptionParams.setTransactionState(state);
        txExceptionParams.setUnitId(unitId);
        report(txExceptionParams);
    }

    private void report(TxExceptionParams exceptionParams) {
        sendUntilNonManager(rpcClient, MessageCreator.writeTxException(exceptionParams), REPORT_ERROR_MESSAGE);
    }

    /**
     * 强通讯
     *
     * @param rpcClient      通讯客户端
     * @param messageDto     通讯数据
     * @param whenNonManager 异常提示
     */
    public static void sendUntilNonManager(RpcClient rpcClient, MessageDto messageDto, String whenNonManager) {
        while (true) {
            try {
                rpcClient.send(rpcClient.loadRemoteKey(), messageDto);
                break;
            } catch (RpcException e) {
                if (e.getCode() == RpcException.NON_TX_MANAGER) {
                    log.error(whenNonManager + ". non tx-manager is alive.");
                    break;
                }
            }
        }
    }

    /**
     * 强通讯
     *
     * @param rpcClient      通讯客户端
     * @param messageDto     通讯数据
     * @param whenNonManager 异常提示
     * @throws RpcException  RpcException
     * @return MessageDto
     */
    public static MessageDto requestUntilNonManager(RpcClient rpcClient, MessageDto messageDto, String whenNonManager) throws RpcException {
        while (true) {
            try {
                return rpcClient.request(rpcClient.loadRemoteKey(), messageDto);
            } catch (RpcException e) {
                if (e.getCode() == RpcException.NON_TX_MANAGER) {
                    throw new RpcException(whenNonManager + ". non tx-manager is alive.");
                }
            }
        }
    }
}
