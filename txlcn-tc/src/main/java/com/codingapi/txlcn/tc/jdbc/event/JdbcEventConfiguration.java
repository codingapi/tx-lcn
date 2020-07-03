package com.codingapi.txlcn.tc.jdbc.event;

import com.codingapi.txlcn.tc.jdbc.log.TransactionLogExecutor;
import com.codingapi.txlcn.tc.jdbc.sql.SqlParserStrategy;
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
    public LcnCommitTransactionJdbcEvent lcnCommitTransactionJdbcEvent(TransactionLogExecutor transactionLogExecutor){
        return new LcnCommitTransactionJdbcEvent(transactionLogExecutor);
    }

    @Bean
    public LcnRollbackTransactionJdbcEvent lcnRollbackTransactionJdbcEvent(TransactionLogExecutor transactionLogExecutor){
        return new LcnRollbackTransactionJdbcEvent(transactionLogExecutor);
    }

    @Bean
    public LcnAfterTransactionJdbcEvent lcnExecuteTransactionJdbcEvent(SqlParserStrategy sqlParserStrategy){
        return new LcnAfterTransactionJdbcEvent(sqlParserStrategy);
    }
}
