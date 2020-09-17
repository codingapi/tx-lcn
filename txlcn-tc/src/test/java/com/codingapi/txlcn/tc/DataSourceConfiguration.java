package com.codingapi.txlcn.tc;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lorne
 * @date 2020/8/8
 * @description
 */
@Configuration
public class DataSourceConfiguration {

    @Bean
        public HikariDataSource dataSource(LcnTestDataSourceConfig config){
            HikariDataSource dataSource =  new HikariDataSource();
            dataSource.setJdbcUrl(config.getJdbcUrl());
            dataSource.setUsername(config.getUsername());
            dataSource.setPassword(config.getPassword());
            dataSource.setDriverClassName(config.getDriverClassName());
            return dataSource;
        }

    @Bean
    public LcnTestDataSourceConfig lcnTestDataSourceConfig(){
        return new LcnTestDataSourceConfig();
    }
}
