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
package com.codingapi.txlcn.tm.core.message;

import com.codingapi.txlcn.tm.config.TxManagerConfig;
import com.codingapi.txlcn.tm.support.TxlcnManagerRpcBeanHelper;
import com.codingapi.txlcn.spi.message.dto.RpcCmd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
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
public class HashGroupRpcCmdHandler implements DisposableBean {
    private final List<ExecutorService> executors;
    private final int concurrentLevel;
    private final TxlcnManagerRpcBeanHelper beanHelper;

    @Autowired
    public HashGroupRpcCmdHandler(TxlcnManagerRpcBeanHelper beanHelper, TxManagerConfig managerConfig) {
        this.concurrentLevel = Math.max(
                (int) (Runtime.getRuntime().availableProcessors() / (1 - 0.8)), managerConfig.getConcurrentLevel());
        log.info("Transaction concurrent level is {}", this.concurrentLevel);
        this.beanHelper = beanHelper;
        this.executors = new ArrayList<>(this.concurrentLevel);
        for (int i = 0; i < this.concurrentLevel; i++) {
            this.executors.add(Executors.newSingleThreadExecutor(r -> new Thread(r, "tx-cmd-executor")));
        }
    }

    void handleMessage(RpcCmd rpcCmd) {
        String groupId = rpcCmd.getMsg().getGroupId();

        // 不需要排队的消息
        if (Objects.isNull(groupId)) {
            log.debug("non fell in message.");
            new RpcCmdTask(beanHelper, rpcCmd).run();
            return;
        }

        // 按事务组hash值从有限的线程池中做出选择
        int index = Math.abs(rpcCmd.getMsg().getGroupId().hashCode() % this.concurrentLevel);
        log.debug("group:{}'s message dispatched executor index: {}", rpcCmd.getMsg().getGroupId(), index);

        // 提交事务消息，处理
        executors.get(index).submit(new RpcCmdTask(beanHelper, rpcCmd));
    }

    @Override
    public void destroy() throws Exception {
        for (ExecutorService executorService : executors) {
            executorService.shutdown();
        }
        for (ExecutorService executorService : executors) {
            try {
                executorService.awaitTermination(6, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
