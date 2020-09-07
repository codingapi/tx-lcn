package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.SqlAnalyseHelper;
import com.codingapi.txlcn.tc.jdbc.database.SqlAnalyseInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import com.codingapi.txlcn.tc.utils.ListUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * @author Gz.
 * @description: 修改语句分析
 * @date 2020-08-13 23:08:26
 */
@Slf4j
public class MysqlUpdateAnalyseStrategy implements SqlSqlAnalyseHandler {

    @Override
    public String mysqlAnalyseStrategy(String sql, Connection connection , Statement stmt) throws SQLException, JSQLParserException {
        String catalog = connection.getCatalog();
        TableList tableList =  DataBaseContext.getInstance().get(catalog);
        Update statement = (Update) stmt;
        Table table = statement.getTable();
        if(!SqlAnalyseHelper.checkTableContainsPk(table, tableList)){
            return sql;
        }
        if(SqlAnalyseHelper.checkWhereContainsPk(table, tableList,statement.getWhere().toString())){
            return sql;
        }
        //TODO now() 之类的函数有待分析
        SqlAnalyseInfo sqlAnalyseInfo = SqlAnalyseHelper.sqlAnalyseSingleTable(tableList, table, statement.getWhere(),statement.getStartJoins());
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String, Object>> query = queryRunner.query(connection, sqlAnalyseInfo.getQuerySql(), new MapListHandler());
        if(ListUtil.isEmpty(query)){
            return sql;
        }
        sql = SqlAnalyseHelper.getNewSql(sql, sqlAnalyseInfo, query);
        log.info("newSql=[{}]",sql);
        return sql;
    }


    @Override
    public void afterPropertiesSet()  {
        AnalyseStrategryFactory.register(MysqlAnalyseEnum.UPDATE.name(), this);
    }
}
