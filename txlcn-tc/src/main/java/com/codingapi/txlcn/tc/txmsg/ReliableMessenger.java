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
import com.codingapi.txlcn.txmsg.dto.MessageDto;
import com.codingapi.txlcn.txmsg.exception.RpcException;

import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 * Date: 19-1-22 下午5:14
 *
 * @author ujued
 */
public interface ReliableMessenger {
    /**
     * 申请锁
     *
     * @param groupId   groupId
     * @param lockIdSet 锁集合
     * @param type      锁类型
     * @return 申请结果
     * @throws RpcException Non TM
     */
    boolean acquireLocks(String groupId, Set<String> lockIdSet, int type) throws RpcException;

    /**
     * 释放锁
     *
     * @param lockIdList 锁集合
     * @throws RpcException Non TM
     */
    void releaseLocks(Set<String> lockIdList) throws RpcException;

    /**
     * 通知事务组
     *
     * @param groupId          groupId
     * @param transactionState 分布式事务状态
     * @return dtx state
     * @throws RpcException         Non TM
     * @throws LcnBusinessException TM Business Err
     */
    int notifyGroup(String groupId, int transactionState) throws RpcException, LcnBusinessException;

    /**
     * 加入事务组
     *
     * @param groupId          groupId
     * @param unitId           事务单元标识
     * @param unitType         事务类型
     * @param transactionState 用户事务状态
     * @throws RpcException         Non TM
     * @throws LcnBusinessException TM Business Err
     */
    void joinGroup(String groupId, String unitId, String unitType, int transactionState) throws RpcException, LcnBusinessException;

    /**
     * 创建事务组
     *
     * @param groupId groupId
     * @throws RpcException         Non TM
     * @throws LcnBusinessException TM Business Err
     */
    void createGroup(String groupId) throws RpcException, LcnBusinessException;

    /**
     * 询问事务状态
     *
     * @param groupId groupId
     * @param unitId  unitId
     * @return 事务无状态 （0,1,-1）
     * @throws RpcException Non TM
     */
    int askTransactionState(String groupId, String unitId) throws RpcException;

    /**
     * 报告失效的TM
     *
     * @param invalidTMSet 失效的TM集合
     * @throws RpcException Non TM
     */
    void reportInvalidTM(HashSet<String> invalidTMSet) throws RpcException;

    /**
     * 查询集群的其他TM实例
     *
     * @return 其他实例集合
     * @throws RpcException Non TM
     */
    HashSet<String> queryTMCluster() throws RpcException;

    /**
     * 发起一个请求
     *
     * @param messageDto 消息
     * @return 响应
     * @throws RpcException Non TM
     */
    MessageDto request(MessageDto messageDto) throws RpcException;

    /**
     * TM集群大小
     *
     * @return size
     */
    int clusterSize();
}
