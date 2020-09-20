package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.statement.Statement;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Gz.
 * @description: SQL分析则略接口
 * @date 2020-08-13 23:08:26
 */
public interface SqlSqlAnalyseHandler  {


    String analyse(String sql, Connection connection, Statement stmt) throws JSQLParserException, SQLException;


    boolean preAnalyse(String sqlType,Statement stmt);

}
