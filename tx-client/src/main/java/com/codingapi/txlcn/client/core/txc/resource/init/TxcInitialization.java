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
package com.codingapi.txlcn.client.core.txc.resource.init;

import com.codingapi.txlcn.client.core.txc.resource.TableStructAnalyser;
import com.codingapi.txlcn.client.core.txc.resource.def.TxcSqlExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
@Component
@Slf4j
public class TxcInitialization implements InitializingBean {

    private final TxcSettingFactory txcSettingFactory;

    private final TableStructAnalyser tableStructAnalyser;

    private final TxcSqlExecutor txcSqlExecutor;

    private final TxcExceptionConnectionPool txcExceptionConnectionPool;

    @Autowired
    public TxcInitialization(TxcSettingFactory txcSettingFactory,
                             TableStructAnalyser tableStructAnalyser,
                             TxcSqlExecutor txcSqlExecutor, TxcExceptionConnectionPool txcExceptionConnectionPool) {
        this.txcSettingFactory = txcSettingFactory;
        this.tableStructAnalyser = tableStructAnalyser;
        this.txcSqlExecutor = txcSqlExecutor;
        this.txcExceptionConnectionPool = txcExceptionConnectionPool;
    }

    @Override
    public void afterPropertiesSet() throws SQLException {
        if (txcSettingFactory.enable()) {
            log.info("enabled txc transaction.");
            if (!tableStructAnalyser.existsTable(txcSettingFactory.lockTableName())) {
                log.info("create lock table.");
                txcSqlExecutor.createLockTable();
            }
            if (!tableStructAnalyser.existsTable(txcSettingFactory.undoLogTableName())) {
                log.info("create undo log table.");
                txcSqlExecutor.createUndoLogTable();
            }
            txcExceptionConnectionPool.init();
        }
    }
}
