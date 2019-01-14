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
//package com.codingapi.tx.client.spi.transaction.txc.resource.init;
//
//import TableStructAnalyser;
//import TxcSqlExecutor;
//import TxcConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * Description:
// * Date: 2018/12/24
// *
// * @author ujued
// */
//@Slf4j
//@Configuration
//public class TxcConfiguration   {
//
//    static {
//        System.setProperty("tx-lcn.client.txc.enable","true");
//    }
//
//    @Bean
//    @ConditionalOnProperty(value = "tx-lcn.client.txc.enable",havingValue = "true")
//    public TxcSettingFactory txcSettingFactory() {
//        return new DefaultTxcSettingFactory();
//    }
//
//    @Bean
//    @ConditionalOnProperty(name = "tx-lcn.client.txc.enable",havingValue = "true")
//    public TxcExceptionConnectionPool txExceptionConnectionPool(DataSourceProperties dataSourceProperties, TxcConfig txcConfig) {
//        return new TxcExceptionConnectionPool(dataSourceProperties, txcConfig);
//    }
//
//    @Bean
//    @ConditionalOnProperty(name = "tx-lcn.client.txc.enable",havingValue = "true")
//    public TxcInitialization txcInitialization(TxcSettingFactory txcSettingFactory,
//                                               TableStructAnalyser tableStructAnalyser,
//                                               TxcSqlExecutor txcSqlExecutor) {
//        return new TxcInitialization(txcSettingFactory, tableStructAnalyser, txcSqlExecutor);
//    }
//
//
//
//}
