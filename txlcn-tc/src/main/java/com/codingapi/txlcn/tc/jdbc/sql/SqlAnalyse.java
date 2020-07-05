package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.p6spy.common.StatementInformation;

/**
 * @author lorne
 * @date 2020/7/3
 * @description
 */
public interface SqlAnalyse {

    String sqlType();

    String analyse(String sql,StatementInformation statementInformation);

}
