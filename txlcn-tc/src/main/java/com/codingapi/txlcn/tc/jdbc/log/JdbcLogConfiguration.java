package com.codingapi.txlcn.tc.jdbc.log;

import com.codingapi.txlcn.tc.jdbc.JdbcTransactionDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Configuration
public class JdbcLogConfiguration {

    @Bean
    public TransactionLogExecutor transactionLogExecutor(LogExecutor logExecutor, JdbcTransactionDataSource jdbcTransactionDataSource){
        return new TransactionLogExecutor(logExecutor,jdbcTransactionDataSource);
    }

    @Bean
    @ConditionalOnProperty(name = "txlcn.tc.sql-type",havingValue = "mysql")
    public LogExecutor mysqlLogExecutor(){
        return new MysqlLogExecutor();
    }
}
