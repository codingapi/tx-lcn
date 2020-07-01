package com.codingapi.txlcn.tc.jdbc.log;

import com.codingapi.txlcn.tc.jdbc.JdbcTransaction;
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

    private LogExecutor logExecutor;

    private QueryRunner queryRunner = new QueryRunner();

    public TransactionLogExecutor(LogExecutor logExecutor) {
        this.logExecutor = logExecutor;
    }

    public void insert(Connection connection,TransactionLog transactionLog) throws SQLException {
        String sql = logExecutor.insert(transactionLog);
        int row = queryRunner.execute(connection,sql,transactionLog.params());
        log.info("insert-sql=>{},row:{}",sql,row);
    }

    public void delete(Connection connection)throws SQLException{
        List<Long> ids =  JdbcTransaction.current().logIds();
        String sql = logExecutor.delete(ids);
        int row = queryRunner.execute(connection,sql);
        log.info("delete-sql=>{},row:{}",sql,row);
    }
}
