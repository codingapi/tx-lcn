package com.codingapi.tx.client.spi.transaction.txc.resource.init;

import com.codingapi.tx.client.spi.transaction.txc.resource.def.config.TxcConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Description:
 * Company: CodingApi
 * Date: 2019/1/14
 *
 * @author codingapi
 */
@Component
public class TxcExceptionConnectionPool {

    private int minIdle;

    private HikariDataSource hikariDataSource;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    public TxcExceptionConnectionPool(TxcConfig txcConfig) {
        this.minIdle = txcConfig.getMinIdle();
    }

    public void init(){
        hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(dataSourceProperties.getUrl());
        hikariDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        hikariDataSource.setUsername(dataSourceProperties.getUsername());
        hikariDataSource.setPassword(dataSourceProperties.getPassword());
        hikariDataSource.setMinimumIdle(minIdle);
    }

    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }

}
