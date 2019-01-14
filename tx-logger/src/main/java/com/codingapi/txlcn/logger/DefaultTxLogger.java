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
package com.codingapi.txlcn.logger;

import com.codingapi.txlcn.logger.db.LogDbProperties;
import com.codingapi.txlcn.logger.db.TxLoggerHelper;
import com.codingapi.txlcn.logger.db.TxLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/26
 *
 * @author codingapi
 */
@Component
@Slf4j
public class DefaultTxLogger implements TxLogger {

    @Value("${spring.application.name}")
    private String appName;

    private final TxLoggerHelper txLoggerHelper;

    private final LogDbProperties dbProperties;

    private final ExecutorService executor;

    @Autowired
    public DefaultTxLogger(LogDbProperties dbProperties, TxLoggerHelper txLoggerHelper) {
        this.dbProperties = dbProperties;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.txLoggerHelper = txLoggerHelper;

        // 等待线程池任务完成
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
            try {
                executor.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException ignored) {
            }
        }));
    }

    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss SSS");
        return format.format(new Date());
    }


    @Override
    public void trace(String groupId, String unitId, String tag, String content) {
        if (dbProperties.isEnabled()) {
            TxLog txLog = new TxLog();
            txLog.setContent(content);
            txLog.setGroupId(groupId);
            txLog.setTag(tag);
            txLog.setUnitId(Objects.isNull(unitId) ? "" : unitId);
            txLog.setAppName(appName);
            txLog.setCreateTime(getTime());
            log.debug("txLoggerInfoEvent->{}", txLog);
            this.executor.execute(() -> txLoggerHelper.insert(txLog));
        }
    }
}
