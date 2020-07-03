package com.codingapi.txlcn.tc.jdbc.sql;

import java.sql.Connection;

/**
 * @author lorne
 * @date 2020/7/3
 * @description
 */
public interface SqlParser {

    String sqlType();

    String parser(String sql, Connection connection);

}
