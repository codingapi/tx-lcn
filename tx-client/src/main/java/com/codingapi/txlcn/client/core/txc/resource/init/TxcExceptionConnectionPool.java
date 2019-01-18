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

import com.codingapi.txlcn.client.core.txc.resource.def.config.TxcConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/14
 *
 * @author codingapi
 */
@Slf4j
public class TxcExceptionConnectionPool {

    private final HikariDataSource hikariDataSource;

    public TxcExceptionConnectionPool(TxcConfig txcConfig) {
        hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(txcConfig.getJdbcUrl());
        hikariDataSource.setDriverClassName(txcConfig.getDriverClassName());
        hikariDataSource.setUsername(txcConfig.getUsername());
        hikariDataSource.setPassword(txcConfig.getPassword());
        hikariDataSource.setMinimumIdle(txcConfig.getMinimumIdle());
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
}
