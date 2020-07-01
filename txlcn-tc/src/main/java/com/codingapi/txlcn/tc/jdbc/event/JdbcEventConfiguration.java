package com.codingapi.txlcn.tc.jdbc.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Configuration
public class JdbcEventConfiguration {

    @Bean
    public LcnCommitTransactionJdbcEvent lcnCommitTransactionJdbcEvent(){
        return new LcnCommitTransactionJdbcEvent();
    }

    @Bean
    public  LcnExecuteTransactionJdbcEvent lcnExecuteTransactionJdbcEvent(){
        return new LcnExecuteTransactionJdbcEvent();
    }
}
