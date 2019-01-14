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
