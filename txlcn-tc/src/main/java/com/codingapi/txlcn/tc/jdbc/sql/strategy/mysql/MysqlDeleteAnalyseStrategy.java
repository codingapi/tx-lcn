package com.codingapi.txlcn.tc.jdbc.sql.strategy.mysql;

import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.SqlAnalyseInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import com.codingapi.txlcn.tc.jdbc.sql.analyse.SqlDetailAnalyse;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.SqlSqlAnalyseHandler;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.chan.*;
import com.codingapi.txlcn.tc.utils.ListUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author Gz.
 * @description: 删除语句分析
 * @date 2020-08-13 23:08:26
 */
@Slf4j
public class MysqlDeleteAnalyseStrategy implements SqlSqlAnalyseHandler {

    private SqlDetailAnalyse sqlDetailAnalyse;

    public MysqlDeleteAnalyseStrategy(SqlDetailAnalyse sqlDetailAnalyse){
        this.sqlDetailAnalyse = sqlDetailAnalyse;
    }

    @Override
    public String analyse(String sql, Connection connection, Statement stmt) throws SQLException, JSQLParserException {
        String catalog = connection.getCatalog();
        TableList tableList =  DataBaseContext.getInstance().get(catalog);
        Delete statement = (Delete) stmt;
        Table table = statement.getTable();
        FilterFacaer filterFacaer = FilterFacaer.builder().tableList(tableList).table(table).deleteStatement(statement).build();
        SqlAnalysqFilterChain filter = new SqlAnalysqFilterChain();
        filter.add(new WhereFilter()).add(new CheckTableContainsPkFilter()).add(new CheckWhereContainsPkFilter());
        if(!filter.doFilter(filterFacaer)){
            return sql;
        }
        SqlAnalyseInfo sqlAnalyseInfo = sqlDetailAnalyse.sqlAnalyseSingleTable(tableList, table, statement.getWhere(),statement.getJoins());
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String, Object>> query = queryRunner.query(connection, sqlAnalyseInfo.getQuerySql(), new MapListHandler());
        if(ListUtil.isEmpty(query)){
            return sql;
        }
        sql = sqlDetailAnalyse.splicingNewSql(sql, sqlAnalyseInfo, query);
        log.info("newSql=[{}]",sql);
        return sql;
    }

    @Override
    public boolean preAnalyse(String sqlType,Statement stmt) {
        return "mysql".equals(sqlType)&&stmt instanceof Delete;
    }

}
