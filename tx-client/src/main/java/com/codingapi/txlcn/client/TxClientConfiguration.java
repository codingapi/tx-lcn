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

import com.codingapi.txlcn.client.support.checking.DTXChecking;
import com.codingapi.txlcn.client.support.checking.SimpleDTXChecking;
import com.codingapi.txlcn.client.support.template.TransactionCleanTemplate;
import com.codingapi.txlcn.commons.runner.TxLcnApplicationRunner;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
    public TxLcnApplicationRunner txLcnApplicationRunner(ApplicationContext applicationContext){
        return new TxLcnApplicationRunner(applicationContext);
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
