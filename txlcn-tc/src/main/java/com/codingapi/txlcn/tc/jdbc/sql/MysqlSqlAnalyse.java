package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lorne
 * @date 2020/7/3
 * @description
 */
@Slf4j
public class MysqlSqlAnalyse implements SqlAnalyse {

    @Override
    public String sqlType() {
        return "mysql";
    }

    @Override
    public String analyse(String sql,StatementInformation statementInformation) {
        log.debug("mysql analyse:{}",sql);
        return sql;
    }
}
