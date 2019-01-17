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

import com.codingapi.txlcn.client.core.txc.resource.def.SqlExecuteInterceptor;
import com.codingapi.txlcn.client.core.txc.resource.def.TxcService;
import com.codingapi.txlcn.client.core.txc.resource.def.TxcSqlExecutor;
import com.codingapi.txlcn.client.core.txc.resource.def.config.TxcConfig;
import com.codingapi.txlcn.client.core.txc.resource.init.MysqlTxcLockSql;
import com.codingapi.txlcn.client.core.txc.resource.init.TxcExceptionConnectionPool;
import com.codingapi.txlcn.client.core.txc.resource.init.TxcLockSql;
import com.codingapi.txlcn.logger.TxLogger;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
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
    public TxcExceptionConnectionPool txcExceptionConnectionPool(TxcConfig txcConfig, DataSourceProperties dataSourceProperties){
        return new TxcExceptionConnectionPool(txcConfig,dataSourceProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public TxcLockSql txcLockSql(){
        return new MysqlTxcLockSql();
    }

    @Bean
    public TableStructAnalyser tableStructAnalyser(DataSource dataSource){
        return new TableStructAnalyser(dataSource);
    }

    @Bean
    public TxcSqlExecutor txcSqlExecutor(TableStructAnalyser tableStructAnalyser,QueryRunner queryRunner, TxcLockSql txcLockSql, TxLogger txLogger)throws Exception{
        return new TxcSqlExecutorImpl(tableStructAnalyser,queryRunner, txcLockSql, txLogger);
    }

    @Bean
    public TxcService txcService(TxcSqlExecutor txcSqlExecutor, TxcExceptionConnectionPool txcExceptionConnectionPool, TxLogger txLogger){
        return new TxcServiceImpl(txcSqlExecutor, txcExceptionConnectionPool, txLogger);
    }

    @Bean
    public SqlExecuteInterceptor sqlExecuteInterceptor(TableStructAnalyser tableStructAnalyser, TxcService txcService){
        return new TxcSqlExecuteInterceptor(tableStructAnalyser, txcService);
    }

    @Bean
    public TxcJdbcEventListener txcJdbcEventListener(SqlExecuteInterceptor sqlExecuteInterceptor){
        return new TxcJdbcEventListener(sqlExecuteInterceptor);
    }

}
