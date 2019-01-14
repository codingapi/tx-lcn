package com.codingapi.txlcn.logger.db;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lorne
 * @date 2018/12/20
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(value = "tx-lcn.logger")
public class LogDbProperties extends HikariConfig {
    private boolean enabled = false;

    @Autowired
    public LogDbProperties(DataSourceProperties dataSourceProperties){
        this.setDriverClassName(dataSourceProperties.getDriverClassName());
        this.setJdbcUrl(dataSourceProperties.getUrl());
        this.setUsername(dataSourceProperties.getUsername());
        this.setPassword(dataSourceProperties.getPassword());
    }
}
