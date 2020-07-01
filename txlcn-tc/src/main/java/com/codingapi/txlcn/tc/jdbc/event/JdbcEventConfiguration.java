package com.codingapi.txlcn.tc.jdbc.event;

import com.codingapi.txlcn.tc.jdbc.log.TransactionLogExecutor;
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
    public  LcnExecuteTransactionJdbcEvent lcnExecuteTransactionJdbcEvent(TransactionLogExecutor transactionLogExecutor){
        return new LcnExecuteTransactionJdbcEvent(transactionLogExecutor);
    }
}
