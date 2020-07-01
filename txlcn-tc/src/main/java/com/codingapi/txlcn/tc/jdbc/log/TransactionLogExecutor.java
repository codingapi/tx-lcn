package com.codingapi.txlcn.tc.jdbc.log;

import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
public class TransactionLogExecutor {

    private LogExecutor logExecutor;

    private QueryRunner queryRunner = new QueryRunner();

    public TransactionLogExecutor(LogExecutor logExecutor) {
        this.logExecutor = logExecutor;
    }

    public void insert(Connection connection,TransactionLog transactionLog) throws SQLException {
        String sql = logExecutor.insert(transactionLog);
        queryRunner.execute(connection,sql,transactionLog.params());
    }

    public void delete(Connection connection,long id)throws SQLException{

    }
}
