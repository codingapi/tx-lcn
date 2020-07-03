package com.codingapi.txlcn.tc.jdbc.log;

import com.codingapi.txlcn.tc.jdbc.JdbcTransaction;
import com.codingapi.txlcn.tc.jdbc.JdbcTransactionDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Slf4j
public class TransactionLogExecutor {

    private JdbcTransactionDataSource jdbcTransactionDataSource;

    private LogExecutor logExecutor;

    private QueryRunner queryRunner = new QueryRunner();

    public TransactionLogExecutor(LogExecutor logExecutor,JdbcTransactionDataSource jdbcTransactionDataSource) {
        this.logExecutor = logExecutor;
        this.jdbcTransactionDataSource = jdbcTransactionDataSource;
    }

    public void saveLog() throws SQLException {
        JdbcTransaction jdbcTransaction = JdbcTransaction.current();
        //todo insert all
        for(TransactionLog transactionLog:jdbcTransaction.getTransactionLogs()) {
            String sql = logExecutor.insert(transactionLog);
            Connection connection = jdbcTransactionDataSource.getConnection();
            int row = queryRunner.execute(connection, sql, transactionLog.params());
            log.debug("insert-sql=>{},row:{},connection:{}", sql, row,connection);
        }
    }

    public void init(Connection connection) throws SQLException{
        try {
            String createSql = logExecutor.create();
            queryRunner.execute(connection, createSql);
            log.info("transaction log init success .");
        }catch (SQLException exception){
            log.warn("init transaction-log");
        }
    }

    public void delete(Connection connection)throws SQLException{
        List<Long> ids =  JdbcTransaction.current().logIds();
        String sql = logExecutor.delete(ids);
        int row = queryRunner.execute(connection,sql);
        log.debug("delete-sql=>{},row:{}",sql,row);
    }
}
