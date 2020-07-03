package com.codingapi.txlcn.tc.jdbc.sql;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;

/**
 * @author lorne
 * @date 2020/7/3
 * @description
 */
@Slf4j
public class MysqlSqlParser implements SqlParser {

    @Override
    public String sqlType() {
        return "mysql";
    }

    @Override
    public String parser(String sql, Connection connection) {
        log.info("mysql parser:{}",sql);
        return sql;
    }
}
