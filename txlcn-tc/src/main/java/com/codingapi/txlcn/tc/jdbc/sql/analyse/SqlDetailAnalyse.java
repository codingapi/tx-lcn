package com.codingapi.txlcn.tc.jdbc.sql.analyse;

import com.codingapi.txlcn.tc.jdbc.database.SqlAnalyseInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Join;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * //todo 若作为各种数据库的适配的话应该还需要做其他的数据库的适配，这样的话还不能满足需求。
 */
public interface SqlDetailAnalyse {

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


    /**
     *
     * @param tableList
     * @param table
     * @param where
     * @param joinList
     * @return
     */
    SqlAnalyseInfo sqlAnalyseSingleTable(TableList tableList, Table table, Expression where, List<Join> joinList);


    /**
     * 拼接新SQL
     * @param sql
     * @param sqlAnalyseInfo
     * @param query
     * @return
     */
    String splicingNewSql(String sql, SqlAnalyseInfo sqlAnalyseInfo, List<Map<String, Object>> query);
}
