package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import net.sf.jsqlparser.JSQLParserException;

import java.sql.SQLException;

public interface MysqlSqlAnalyseStrategry {

    String MysqlAnalyseStrategry(String sql , StatementInformation statementInformation) throws JSQLParserException, SQLException;
}
