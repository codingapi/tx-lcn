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
package com.codingapi.txlcn.logger.config;

import com.codingapi.txlcn.logger.NoTxLogger;
import com.codingapi.txlcn.logger.TxLogger;
import com.codingapi.txlcn.logger.db.DefaultTxLogger;
import com.codingapi.txlcn.logger.db.LogDbHelper;
import com.codingapi.txlcn.logger.db.LogDbProperties;
import com.codingapi.txlcn.logger.db.TxLcnLoggerHelper;
import com.codingapi.txlcn.logger.ex.TxLoggerException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author meetzy
 */
@Configuration
public class LogDBConfiguration {
    
    @Bean
    @ConditionalOnProperty(value = "tx-lcn.logger.enabled", havingValue = "true")
    public LogDbHelper logDbHelper(LogDbProperties logDbProperties) throws TxLoggerException {
        return new LogDbHelper(logDbProperties);
    }
    
    @Bean
    @ConfigurationProperties(value = "tx-lcn.logger")
    public LogDbProperties logDbProperties(DataSourceProperties dataSourceProperties) {
        return new LogDbProperties(dataSourceProperties);
    }
    
    @Bean
    @ConditionalOnBean(LogDbHelper.class)
    public TxLcnLoggerHelper txLcnLoggerHelper(LogDbHelper logDbHelper) {
        return new TxLcnLoggerHelper(logDbHelper);
    }
    
    @Bean
    @ConditionalOnProperty(value = "tx-lcn.logger.enabled", havingValue = "true")
    public TxLogger txLogger(LogDbProperties logDbProperties, TxLcnLoggerHelper txLcnLoggerHelper) {
        return new DefaultTxLogger(logDbProperties, txLcnLoggerHelper);
    }
    
    @Bean
    @ConditionalOnMissingBean
    public TxLogger txLogger() {
        return new NoTxLogger();
    }
}
