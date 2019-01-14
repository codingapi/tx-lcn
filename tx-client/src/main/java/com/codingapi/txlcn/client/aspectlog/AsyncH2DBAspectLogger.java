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
package com.codingapi.txlcn.client.aspectlog;

import com.codingapi.txlcn.commons.bean.TransactionInfo;
import com.codingapi.txlcn.commons.exception.SerializerException;
import com.codingapi.txlcn.commons.util.serializer.SerializerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Description: 切面日志的异步操作
 * Date: 2018/12/28
 *
 * @author ujued
 * @see AspectLogger
 */
@Component
@Slf4j
public class AsyncH2DBAspectLogger implements AspectLogger {

    private static final ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    private final AspectLogHelper txLogHelper;

    @Autowired
    public AsyncH2DBAspectLogger(AspectLogHelper txLogHelper) {
        this.txLogHelper = txLogHelper;

        // 等待线程池任务完成
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executorService.shutdown();
            try {
                executorService.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException ignored) {
            }
        }));
    }

    @Override
    public void trace(String groupId, String unitId, TransactionInfo transactionInfo) {
        executorService.submit(() -> {
            long t1 = System.currentTimeMillis();
            log.debug("event-save-start->{}", groupId);
            byte[] bytes;
            try {
                bytes = SerializerContext.getInstance().serialize(transactionInfo);
            } catch (SerializerException e) {
                e.printStackTrace();
                return;
            }
            AspectLog txLog = new AspectLog();
            txLog.setBytes(bytes);
            txLog.setGroupId(groupId);
            txLog.setUnitId(unitId);
            txLog.setMethodStr(transactionInfo.getMethodStr());
            txLog.setTime(System.currentTimeMillis());
            txLog.setUnitIdHash(groupId.hashCode());
            txLog.setUnitIdHash(unitId.hashCode());

            boolean res = txLogHelper.save(txLog);
            long t2 = System.currentTimeMillis();
            log.debug("event-save-over ok:{} ->{},time:{}", res, groupId, (t2 - t1));
        });
    }

    @Override
    public void clearLog(String groupId, String unitId) {
        executorService.submit(() -> {
            long t1 = System.currentTimeMillis();
            log.debug("event-clear-start->{}", groupId);
            boolean res = txLogHelper.delete(groupId.hashCode(), unitId.hashCode());
            long t2 = System.currentTimeMillis();
            log.debug("event-clear-over ok:{} ->{},time:{}", res, groupId, (t2 - t1));
        });
    }
}
