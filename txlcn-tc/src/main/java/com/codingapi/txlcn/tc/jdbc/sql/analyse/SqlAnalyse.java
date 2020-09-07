package com.codingapi.txlcn.tc.jdbc.sql.analyse;

import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.statement.insert.Insert;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlAnalyse {

    /**
     * 单表插入分析
     * @param sql
     * @param connection
     * @param statement
     * @param itemsList
     * @param pk
     * @param pkIndex
     * @return
     * @throws SQLException
     */
    boolean singleInsertAnalyse(String sql, Connection connection, Insert statement, ItemsList itemsList, String pk, int pkIndex) throws SQLException;

    /**
     * 多表插入分析
     * @param sql
     * @param connection
     * @param statement
     * @param itemsList
     * @param pk
     * @param pkIndex
     * @return
     * @throws SQLException
     */
    boolean multiInsertAnalyse(String sql, Connection connection, Insert statement, ItemsList itemsList, String pk, int pkIndex) throws SQLException;
}
