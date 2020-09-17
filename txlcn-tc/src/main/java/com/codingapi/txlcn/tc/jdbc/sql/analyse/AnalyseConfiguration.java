package com.codingapi.txlcn.tc.jdbc.sql.analyse;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lorne
 * @date 2020/9/17
 * @description
 */
@Configurable
public class AnalyseConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SqlDetailAnalyse mysqlSqlDetailAnalyse(){
        return new MysqlSqlDetailAnalyse();
    }



}
