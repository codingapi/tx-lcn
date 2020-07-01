package com.codingapi.txlcn.tc.jdbc.event;

import com.codingapi.txlcn.tc.TransactionConstant;
import com.codingapi.txlcn.tc.jdbc.TransactionJdbcEvent;
import com.codingapi.txlcn.tc.jdbc.TransactionJdbcState;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Slf4j
public class LcnExecuteTransactionJdbcEvent implements TransactionJdbcEvent {

    @Override
    public String type() {
        return TransactionConstant.LCN;
    }

    @Override
    public TransactionJdbcState state() {
        return TransactionJdbcState.EXECUTE;
    }

    @Override
    public Object execute(Connection connection, Object param) throws SQLException {
        String sql = (String) param;
        log.info("sql=>{}",sql);
        return sql;
    }
}
