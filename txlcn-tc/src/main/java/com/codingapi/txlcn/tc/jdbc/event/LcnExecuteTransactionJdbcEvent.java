package com.codingapi.txlcn.tc.jdbc.event;

import com.codingapi.txlcn.tc.TransactionConstant;
import com.codingapi.txlcn.tc.jdbc.JdbcTransaction;
import com.codingapi.txlcn.tc.jdbc.TransactionJdbcEvent;
import com.codingapi.txlcn.tc.jdbc.TransactionJdbcState;
import com.codingapi.txlcn.tc.jdbc.log.TransactionLog;
import com.codingapi.txlcn.tc.jdbc.log.TransactionLogExecutor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Slf4j
@AllArgsConstructor
public class LcnExecuteTransactionJdbcEvent implements TransactionJdbcEvent {

    private TransactionLogExecutor transactionLogExecutor;

    @Override
    public String type() {
        return TransactionConstant.LCN;
    }

    @Override
    public TransactionJdbcState state() {
        return TransactionJdbcState.EXECUTE;
    }

    @Override
    public Object execute(Object param) throws SQLException {
        String sql = (String) param;
        Connection connection = JdbcTransaction.current().getConnection();
        log.info("connection:{}",connection);
        log.info("sql=>{}",sql);

        TransactionLog transactionLog = new TransactionLog(sql);
        transactionLogExecutor.insert(connection,transactionLog);
        JdbcTransaction.current().add(transactionLog);
        return sql;
    }
}
