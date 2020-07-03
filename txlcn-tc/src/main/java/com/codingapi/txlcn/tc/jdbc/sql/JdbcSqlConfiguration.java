package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.tc.config.TxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lorne
 * @date 2020/7/3
 * @description
 */
@Configuration
public class JdbcSqlConfiguration {

    @Bean
    public SqlParserStrategy sqlParserContext(@Autowired(required = false) List<SqlParser> sqlParsers, TxConfig txConfig){
        return new SqlParserStrategy(sqlParsers,txConfig);
    }

    @Bean
    public SqlParser mysqlSqlParser(){
        return new MysqlSqlParser();
    }
}
