package com.codingapi.txlcn.tc.jdbc.event;

import com.codingapi.txlcn.p6spy.event.JdbcCallable;
import com.codingapi.txlcn.tc.TransactionConstant;
import com.codingapi.txlcn.tc.jdbc.TransactionJdbcEvent;
import com.codingapi.txlcn.tc.jdbc.TransactionJdbcState;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
public class LcnCommitTransactionJdbcEvent implements TransactionJdbcEvent {

    @Override
    public String type() {
        return TransactionConstant.LCN;
    }

    @Override
    public TransactionJdbcState state() {
        return TransactionJdbcState.COMMIT;
    }

    @Override
    public Object execute(Connection connection, Object param) throws SQLException {
        JdbcCallable jdbcCallable = (JdbcCallable) param;
        jdbcCallable.call();
        //不需要返回值，返回固定值1
        return 1;
    }
}
