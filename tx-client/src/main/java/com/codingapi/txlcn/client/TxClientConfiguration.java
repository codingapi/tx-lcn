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
package com.codingapi.txlcn.client;

import com.codingapi.txlcn.client.core.txc.resource.init.DefaultTxcSettingFactory;
import com.codingapi.txlcn.client.core.txc.resource.init.TxcSettingFactory;
import com.codingapi.txlcn.client.support.checking.DTXChecking;
import com.codingapi.txlcn.client.support.checking.SimpleDTXChecking;
import com.codingapi.txlcn.client.support.common.template.TransactionCleanTemplate;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/12/10
 *
 * @author lorne
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties
public class TxClientConfiguration {


    @Bean
    public QueryRunner queryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public TxcSettingFactory txcSettingFactory() {
        return new DefaultTxcSettingFactory();
    }


    @Bean
    public SmartInitializingSingleton dtxCheckingTransactionCleanTemplateAdapter(DTXChecking dtxChecking,
                                                                                 TransactionCleanTemplate transactionCleanTemplate) {
        if (dtxChecking instanceof SimpleDTXChecking) {
            return () -> ((SimpleDTXChecking) dtxChecking).setTransactionCleanTemplate(transactionCleanTemplate);
        }
        return () -> {
        };
    }
}
