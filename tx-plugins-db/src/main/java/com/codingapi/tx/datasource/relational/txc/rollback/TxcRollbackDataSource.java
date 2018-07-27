package com.codingapi.tx.datasource.relational.txc.rollback;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jsy.
 * @title
 * @time 17/12/26.
 */
@Component
public class TxcRollbackDataSource {

    private Map<String, DataSource> dataSourceMap = new HashMap<>();

    public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    public Connection getConnectionByDbName(String dbName) throws SQLException {

        DataSource dataSource = dataSourceMap.get(dbName);
        if (dataSource == null) {
            throw new SQLException("datasource do not exist, name: " + dbName);
        }

        return dataSourceMap.get(dbName).getConnection();
    }
}
