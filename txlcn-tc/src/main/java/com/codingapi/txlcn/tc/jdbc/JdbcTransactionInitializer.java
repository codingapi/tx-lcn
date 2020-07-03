package com.codingapi.txlcn.tc.jdbc;

import com.codingapi.txlcn.tc.jdbc.log.TransactionLogExecutor;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class JdbcTransactionInitializer {

    private TransactionLogExecutor transactionLogExecutor;

    public void init(Connection connection) throws SQLException{
        //todo sql-meta init

        //初始化事务日志表
        transactionLogExecutor.init(connection);


        connection.close();
    }
}
