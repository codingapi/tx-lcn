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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/14
 *
 * @author codingapi
 */
@Component
public class TxcExceptionConnectionPool {

    private int minIdle;

    private HikariDataSource hikariDataSource;

    private final DataSourceProperties dataSourceProperties;

    @Autowired
    public TxcExceptionConnectionPool(TxcConfig txcConfig, DataSourceProperties dataSourceProperties) {
        this.minIdle = txcConfig.getMinIdle();
        this.dataSourceProperties = dataSourceProperties;
    }

    public void init(){
        hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(dataSourceProperties.getUrl());
        hikariDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        hikariDataSource.setUsername(dataSourceProperties.getUsername());
        hikariDataSource.setPassword(dataSourceProperties.getPassword());
        hikariDataSource.setMinimumIdle(minIdle);
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

}
