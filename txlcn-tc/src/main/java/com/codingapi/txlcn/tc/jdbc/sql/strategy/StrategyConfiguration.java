package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.codingapi.txlcn.tc.jdbc.sql.analyse.SqlDetailAnalyse;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.mysql.MysqlDeleteAnalyseStrategy;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.mysql.MysqlInsertAnalyseStrategy;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.mysql.MysqlUpdateAnalyseStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lorne
 * @date 2020/9/17
 * @description
 */
@Configurable
public class StrategyConfiguration {

    @Bean
    public AnalyseStrategryFactory analyseStrategryFactory(@Autowired(required = false)List<SqlSqlAnalyseHandler> analyseHandlers){
        return new AnalyseStrategryFactory(analyseHandlers);
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSqlAnalyseHandler mysqlInsertAnalyseStrategy(SqlDetailAnalyse sqlDetailAnalyse){
        return new MysqlInsertAnalyseStrategy(sqlDetailAnalyse);
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSqlAnalyseHandler mysqlDeleteAnalyseStrategy(SqlDetailAnalyse sqlDetailAnalyse){
        return new MysqlDeleteAnalyseStrategy(sqlDetailAnalyse);
    }

    @Bean
    @ConditionalOnMissingBean
    public SqlSqlAnalyseHandler mysqlUpdateAnalyseStrategy(SqlDetailAnalyse sqlDetailAnalyse){
        return new MysqlUpdateAnalyseStrategy(sqlDetailAnalyse);
    }


}
