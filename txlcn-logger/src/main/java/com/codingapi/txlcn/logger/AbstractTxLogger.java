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

import com.codingapi.txlcn.commons.util.Transactions;
import com.codingapi.txlcn.logger.db.TxLog;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * Date: 19-1-25 下午3:08
 *
 * @author ujued
 */
@Slf4j
public abstract class AbstractTxLogger implements TxLogger {

    private ExecutorService loggerSaveService;

    public AbstractTxLogger() {
        this.loggerSaveService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void trace(String groupId, String unitId, String tag, String content) {
        TxLog txLog = new TxLog();
        txLog.setContent(content);
        txLog.setGroupId(groupId);
        txLog.setTag(tag);
        txLog.setUnitId(Objects.isNull(unitId) ? "" : unitId);
        txLog.setAppName(Transactions.APPLICATION_ID_WHEN_RUNNING);
        txLog.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()));
        log.debug("trace tx-logger -> {}", txLog);
        this.loggerSaveService.execute(() -> saveTrace(txLog));
    }

    public abstract void saveTrace(TxLog txLog);
}
