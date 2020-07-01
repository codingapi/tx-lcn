package com.codingapi.txlcn.tc.sql;

import com.codingapi.txlcn.p6spy.CompoundJdbcEventListener;
import com.codingapi.txlcn.p6spy.event.P6spyJdbcEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Configuration
public class SqlConfiguration {


    @Bean
    public CompoundJdbcEventListener compoundJdbcEventListener(TransactionJdbcEventListener transactionJdbcEventListener){
        return new CompoundJdbcEventListener(transactionJdbcEventListener);
    }

    @Bean
    public TransactionJdbcEventListener transactionJdbcEventListener(){
        return new TransactionJdbcEventListener();
    }

}
