package com.codingapi.txlcn.tc.jdbc;

import com.codingapi.txlcn.p6spy.CompoundJdbcEventListener;
import com.codingapi.txlcn.tc.jdbc.log.TransactionLogExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Configuration
public class JdbcConfiguration {


    @Bean
    public CompoundJdbcEventListener compoundJdbcEventListener(TransactionJdbcEventListener transactionJdbcEventListener){
        return new CompoundJdbcEventListener(transactionJdbcEventListener);
    }

    @Bean
    public TransactionJdbcEventListener transactionJdbcEventListener(@Autowired(required = false) List<TransactionJdbcEvent> transactionJdbcEvents){
        return new TransactionJdbcEventListener(transactionJdbcEvents);
    }

    @Bean
    public JdbcTransactionDataSource jdbcTransactionDataSource(){
        return new JdbcTransactionDataSource();
    }

    @Bean
    public JdbcTransactionInitializer jdbcTransactionInitializer(TransactionLogExecutor transactionLogExecutor){
        return new JdbcTransactionInitializer(transactionLogExecutor);
    }


}
