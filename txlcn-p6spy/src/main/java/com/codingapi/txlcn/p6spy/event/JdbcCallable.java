package com.codingapi.txlcn.p6spy.event;

import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@FunctionalInterface
public interface JdbcCallable {
    void call() throws SQLException;
}
