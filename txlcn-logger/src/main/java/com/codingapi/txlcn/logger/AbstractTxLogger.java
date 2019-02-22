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

import com.codingapi.txlcn.common.util.SpringUtils;
import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.logger.db.LogDbProperties;
import com.codingapi.txlcn.logger.db.TxLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

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
public abstract class AbstractTxLogger implements TxLogger {

    private final ExecutorService loggerSaveService;

    private final Logger LOG;

    private LogDbProperties logDbProperties;

    public AbstractTxLogger(Class<?> className) {
        this.loggerSaveService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        this.LOG = LoggerFactory.getLogger(className);
    }

    @Override
    public void trace(String groupId, String unitId, String tag, String content, Object... args) {
        if (Objects.isNull(logDbProperties)) {
            logDbProperties = SpringUtils.getBean(LogDbProperties.class);
        }
        if (logDbProperties.isEnabled() && !logDbProperties.isOnlyError()) {
            saveTxLog(groupId, unitId, tag, content, args);
        }
        LOG.debug(content + " @group(" + groupId + ")", args);
    }

    @Override
    public void error(String groupId, String unitId, String tag, String content, Object... args) {
        if (Objects.isNull(logDbProperties)) {
            logDbProperties = SpringUtils.getBean(LogDbProperties.class);
        }
        if (logDbProperties.isEnabled() && logDbProperties.isOnlyError()) {
            saveTxLog(groupId, unitId, tag, content, args);
        }
        LOG.error(content + " @group(" + groupId + ")", args);
    }

    private void saveTxLog(String groupId, String unitId, String tag, String content, Object... args) {
        TxLog txLog = new TxLog();
        txLog.setContent(content);
        txLog.setArgs(args);
        txLog.setTag(tag);
        txLog.setGroupId(StringUtils.isEmpty(groupId) ? "" : groupId);
        txLog.setUnitId(StringUtils.isEmpty(unitId) ? "" : unitId);
        txLog.setAppName(Transactions.APPLICATION_ID_WHEN_RUNNING);
        txLog.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(new Date()));
        this.loggerSaveService.execute(() -> saveLog(txLog));
    }

    public abstract void saveLog(TxLog txLog);
}
