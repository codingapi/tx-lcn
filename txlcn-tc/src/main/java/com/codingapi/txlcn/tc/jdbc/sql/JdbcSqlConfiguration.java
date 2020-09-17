package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.AnalyseStrategryFactory;
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
    public SqlAnalyseStrategy sqlParserContext(@Autowired(required = false) List<SqlAnalyse> sqlAnalyses, TxConfig txConfig){
        return new SqlAnalyseStrategy(sqlAnalyses,txConfig);
    }

    @Bean
    public SqlAnalyse mysqlSqlParser(AnalyseStrategryFactory analyseStrategryFactory){
        return new MysqlSqlAnalyse(analyseStrategryFactory);
    }
}
