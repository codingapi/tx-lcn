package com.codingapi.txlcn.tc.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/2
 * @description
 */
public class JdbcTransactionDataSource {

    private Connection connection;

    public void setConnection(Connection connection) throws SQLException{
        this.connection = connection;
        this.connection.setAutoCommit(true);
    }

    public Connection getConnection(){
        return connection;
    }

    public boolean noConnection() throws SQLException {
        return connection==null||connection.isClosed();
    }
}
