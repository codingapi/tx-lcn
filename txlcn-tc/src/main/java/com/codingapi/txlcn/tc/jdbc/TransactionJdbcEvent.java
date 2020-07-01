package com.codingapi.txlcn.tc.jdbc;

import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
public interface TransactionJdbcEvent {

    String type();

    TransactionJdbcState state();

    Object execute(Object param) throws SQLException;
}
