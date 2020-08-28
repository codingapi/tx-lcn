package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.JdbcAnalyseUtils;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import com.codingapi.txlcn.tc.utils.ListUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Gz.
 * @description: 删除语句分析
 * @date 2020-08-13 23:08:26
 */
@Slf4j
public class MysqlDeleteAnalyseStrategry implements MysqlSqlAnalyseStrategry {

    @Override
    public String mysqlAnalyseStrategry(String sql, Connection connection) throws SQLException, JSQLParserException {
        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog, JdbcAnalyseUtils.analyse(connection));
        TableList tableList =  DataBaseContext.getInstance().get(catalog);
        Delete statement = (Delete) CCJSqlParserUtil.parse(sql);
        Table table = statement.getTable();
        if(!SqlAnalyseHelper.checkTableContainsPk(table, tableList)){
            return sql;
        }
        if(SqlAnalyseHelper.checkWhereContainsPk(table, tableList,statement.getWhere().toString())){
            return sql;
        }
        SqlAnalyseInfo sqlAnalyseInfo = SqlAnalyseHelper.sqlAnalyseSingleTable(tableList, table, statement.getWhere(),statement.getJoins());
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String, Object>> query = queryRunner.query(connection, sqlAnalyseInfo.getQuerySql(), new MapListHandler());
        if(ListUtil.isEmpty(query)){
            return sql;
        }
        sql = SqlAnalyseHelper.getNewSql(sql, sqlAnalyseInfo, query);
        log.info("newSql=[{}]",sql);
        return sql;
    }




}
