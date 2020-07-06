package com.codingapi.txlcn.tc.jdbc.event;

import com.codingapi.txlcn.p6spy.event.JdbcCallable;
import com.codingapi.txlcn.tc.TransactionConstant;
import com.codingapi.txlcn.tc.jdbc.JdbcTransaction;
import com.codingapi.txlcn.tc.jdbc.TransactionJdbcEvent;
import com.codingapi.txlcn.tc.jdbc.TransactionJdbcState;
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
public class LcnRollbackTransactionJdbcEvent implements TransactionJdbcEvent {

    private TransactionLogExecutor transactionLogExecutor;

    @Override
    public String type() {
        return TransactionConstant.LCN;
    }

    @Override
    public TransactionJdbcState state() {
        return TransactionJdbcState.ROLLBACK;
    }

    @Override
    public Object execute(Object param) throws SQLException {
        Connection connection = JdbcTransaction.current().getConnection();
        log.info("rollback connection:{}",connection);
        JdbcCallable jdbcCallable = (JdbcCallable) param;

        //不需要返回值，返回固定值1
        return 1;
    }
}
