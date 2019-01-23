package com.codingapi.txlcn.tc;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Description:
 * Date: 19-1-23 上午11:12
 *
 * @author ujued
 */
@Configuration
public class MiniConfiguration {

    @Bean
    public DataSource dataSource() {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        hikariDataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/txlcn-demo?characterEncoding=UTF-8&serverTimezone=UTC");
        hikariDataSource.setUsername("root");
        hikariDataSource.setPassword("123456");
        return hikariDataSource;
    }
}
