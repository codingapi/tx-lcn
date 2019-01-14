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
package com.codingapi.txlcn.manager.core.message;

import com.codingapi.txlcn.manager.config.TxManagerConfig;
import com.codingapi.txlcn.manager.support.ManagerRpcBeanHelper;
import com.codingapi.txlcn.spi.message.dto.RpcCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Date: 2018/12/18
 *
 * @author ujued
 */
@Slf4j
@Component
public class HashGroupRpcCmdHandler {
    private final List<ExecutorService> executors;
    private final int concurrentLevel;
    private final ManagerRpcBeanHelper beanHelper;

    @Autowired
    public HashGroupRpcCmdHandler(ManagerRpcBeanHelper beanHelper, TxManagerConfig managerConfig) {
        this.concurrentLevel = Math.max(
                (int) (Runtime.getRuntime().availableProcessors() / (1 - 0.8)), managerConfig.getConcurrentLevel());
        log.info("concurrent level is {}", this.concurrentLevel);
        this.beanHelper = beanHelper;
        this.executors = new ArrayList<>(this.concurrentLevel);
        for (int i = 0; i < this.concurrentLevel; i++) {
            this.executors.add(Executors.newSingleThreadExecutor(r -> new Thread(r, "tx-cmd-executor")));
        }

        // 等待线程池任务完成
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (ExecutorService executorService : executors) {
                executorService.shutdown();
            }
            for (ExecutorService executorService : executors) {
                try {
                    executorService.awaitTermination(10, TimeUnit.MINUTES);
                } catch (InterruptedException ignored) {
                }
            }
        }));

    }

    public void handleMessage(RpcCmd rpcCmd) {
        // 按事务组hash值从有限的线程池中做出选择
        String groupId = rpcCmd.getMsg().getGroupId();
        if (Objects.isNull(groupId)) {
            throw new IllegalStateException("bad request! message's groupId not nullable!");
        }
        int index = Math.abs(rpcCmd.getMsg().getGroupId().hashCode() % this.concurrentLevel);
        log.info("group:{}'s message dispatched executor index: {}", rpcCmd.getMsg().getGroupId(), index);

        // 提交事务消息，处理
        executors.get(index).submit(new RpcCmdTask(beanHelper, rpcCmd));
    }
}
