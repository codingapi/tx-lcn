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
package com.codingapi.txlcn.client.core.txc.resource;

import com.codingapi.txlcn.client.core.txc.resource.def.config.TxcConfig;
import com.codingapi.txlcn.client.core.txc.resource.init.TxcExceptionConnectionPool;
import com.codingapi.txlcn.client.core.txc.resource.init.TxcMysql;
import com.codingapi.txlcn.client.core.txc.resource.init.TxcSql;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Description:
 * Date: 2018/12/24
 *
 * @author ujued
 */
@Configuration
public class TxcConfiguration {

    @Bean
    public QueryRunner queryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }

    @Bean
    public TxcExceptionConnectionPool txcExceptionConnectionPool(TxcConfig txcConfig) {
        return new TxcExceptionConnectionPool(txcConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public TxcSql txcLockSql() {
        return new TxcMysql();
    }
}
