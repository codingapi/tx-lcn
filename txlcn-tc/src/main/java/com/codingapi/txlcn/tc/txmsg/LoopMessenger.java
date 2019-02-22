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
package com.codingapi.txlcn.tc.txmsg;

import com.codingapi.txlcn.common.exception.LcnBusinessException;
import com.codingapi.txlcn.tc.config.TxClientConfig;
import com.codingapi.txlcn.txmsg.MessageConstants;
import com.codingapi.txlcn.txmsg.RpcClient;
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.exception.RpcException;
import com.codingapi.txlcn.txmsg.params.JoinGroupParams;
import com.codingapi.txlcn.txmsg.params.NotifyGroupParams;
import com.codingapi.txlcn.txmsg.util.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 * Date: 19-1-22 下午5:12
 *
 * @author ujued
 */
@Component
@Slf4j
public class LoopMessenger implements ReliableMessenger {

    private final RpcClient rpcClient;

    private final TxClientConfig clientConfig;

    @Autowired
    public LoopMessenger(RpcClient rpcClient, TxClientConfig clientConfig) {
        this.rpcClient = rpcClient;
        this.clientConfig = clientConfig;
    }

    @Override
    public boolean acquireLocks(String groupId, Set<String> lockIdSet, int type) throws RpcException {
        MessageDto messageDto = request(MessageCreator.acquireLocks(groupId, lockIdSet, type));
        return MessageUtils.statusOk(messageDto);
    }

    @Override
    public void releaseLocks(Set<String> lockIdList) throws RpcException {
        MessageDto messageDto = request(MessageCreator.releaseLocks(lockIdList));
        if (!MessageUtils.statusOk(messageDto)) {
            throw new RpcException("release locks fail.");
        }
    }

    @Override
    public int notifyGroup(String groupId, int transactionState) throws RpcException, LcnBusinessException {
        NotifyGroupParams notifyGroupParams = new NotifyGroupParams();
        notifyGroupParams.setGroupId(groupId);
        notifyGroupParams.setState(transactionState);
        MessageDto messageDto = request0(MessageCreator.notifyGroup(notifyGroupParams),
                clientConfig.getTmRpcTimeout() * clientConfig.getChainLevel());
        // 成功清理发起方事务
        if (!MessageUtils.statusOk(messageDto)) {
            throw new LcnBusinessException(messageDto.loadBean(Throwable.class));
        }
        return messageDto.loadBean(Integer.class);
    }

    @Override
    public void joinGroup(String groupId, String unitId, String unitType, int transactionState) throws RpcException, LcnBusinessException {
        JoinGroupParams joinGroupParams = new JoinGroupParams();
        joinGroupParams.setGroupId(groupId);
        joinGroupParams.setUnitId(unitId);
        joinGroupParams.setUnitType(unitType);
        joinGroupParams.setTransactionState(transactionState);
        MessageDto messageDto = request(MessageCreator.joinGroup(joinGroupParams));
        if (!MessageUtils.statusOk(messageDto)) {
            throw new LcnBusinessException(messageDto.loadBean(Throwable.class));
        }
    }

    @Override
    public void createGroup(String groupId) throws RpcException, LcnBusinessException {
        // TxManager创建事务组
        MessageDto messageDto = request(MessageCreator.createGroup(groupId));
        if (!MessageUtils.statusOk(messageDto)) {
            throw new LcnBusinessException(messageDto.loadBean(Throwable.class));
        }
    }

    @Override
    public int askTransactionState(String groupId, String unitId) throws RpcException {
        MessageDto messageDto = request(MessageCreator.askTransactionState(groupId, unitId));
        if (MessageUtils.statusOk(messageDto)) {
            return messageDto.loadBean(Integer.class);
        }
        return -1;
    }

    @Override
    public void reportInvalidTM(HashSet<String> invalidTMSet) throws RpcException {
        MessageDto messageDto = new MessageDto();
        messageDto.setAction(MessageConstants.ACTION_CLEAN_INVALID_TM);
        messageDto.setData(invalidTMSet);
        messageDto = request(messageDto);
        if (!MessageUtils.statusOk(messageDto)) {
            throw new RpcException(messageDto.loadBean(Throwable.class));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public HashSet<String> queryTMCluster() throws RpcException {
        MessageDto messageDto = new MessageDto();
        messageDto.setAction(MessageConstants.ACTION_QUERY_TM_CLUSTER);
        messageDto = request(messageDto);
        if (MessageUtils.statusOk(messageDto)) {
            return messageDto.loadBean(HashSet.class);
        }
        throw new RpcException(messageDto.loadBean(Throwable.class));
    }

    @Override
    public MessageDto request(MessageDto messageDto) throws RpcException {
        return request0(messageDto, -1);
    }

    private MessageDto request0(MessageDto messageDto, long timeout) throws RpcException {
        return request(messageDto, timeout, "request fail");
    }

    @Override
    public int clusterSize() {
        return rpcClient.loadAllRemoteKey().size();
    }


    /**
     * 强通讯
     *
     * @param messageDto            通讯数据
     * @param whenNonManagerMessage 异常提示
     * @return MessageDto
     * @throws RpcException RpcException
     */
    private MessageDto request(MessageDto messageDto, long timeout, String whenNonManagerMessage) throws RpcException {
        for (int i = 0; i < rpcClient.loadAllRemoteKey().size() + 1; i++) {
            try {
                String remoteKey = rpcClient.loadRemoteKey();
                MessageDto result = rpcClient.request(remoteKey, messageDto, timeout);
                log.debug("request action: {}. TM[{}]", messageDto.getAction(), remoteKey);
                return result;
            } catch (RpcException e) {
                if (e.getCode() == RpcException.NON_TX_MANAGER) {
                    throw new RpcException(e.getCode(), whenNonManagerMessage + ". non tx-manager is alive.");
                }
            }
        }
        throw new RpcException(RpcException.NON_TX_MANAGER, whenNonManagerMessage + ". non tx-manager is alive.");
    }
}
