package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.JdbcAnalyseUtils;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import com.codingapi.txlcn.tc.utils.ListUtil;
import com.google.common.collect.Maps;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;


/**
 * @author Gz.
 * @description: 修改语句分析
 * @date 2020-08-13 23:08:26
 */
@Slf4j
public class MysqlUpdateAnalyseStrategry implements MysqlSqlAnalyseStrategry {

    @Override
    public String MysqlAnalyseStrategry(String sql, StatementInformation statementInformation) throws SQLException, JSQLParserException {

        Connection connection = statementInformation.getConnectionInformation().getConnection();
        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog, JdbcAnalyseUtils.analyse(connection));
        TableList tableList =  DataBaseContext.getInstance().get(catalog);
        Update statement = (Update) CCJSqlParserUtil.parse(sql);
        if(!checkTableContainsPk(statement,tableList)){
            //TODO
            return sql;
        }
        String selectSql;
        Map<String,String> sqlMap = Maps.newHashMap();
        if(statement.getTables().size() == 1){
            sqlMap = updateSqlAnalyseSingleTable(tableList, statement,sql);
        }else {
            sqlMap = updateSqlAnalyseMultiTable(tableList, statement,sql);
        }
        String select = sqlMap.get("select");
        String querySql = sqlMap.get("querySql");
        String primaryKey = sqlMap.get("primaryKey");
        QueryRunner queryRunner = new QueryRunner();
        List<Map<String, Object>> query = queryRunner.query(connection, querySql, new MapListHandler());
        sql = sql.concat(" and ").concat(select).concat(" in ( ");

        int size = query.size();
        for (int i = 0; i < query.size(); i++) {
            sql =  sql.concat(query.get(i).get(primaryKey).toString());
            if(i + 1 < size){
                sql = sql.concat(" , ");
            }
        }
        sql = sql.concat(")");
        return sql;
    }

    private  Map<String,String> updateSqlAnalyseMultiTable(TableList tableList, Update statement, String sql) {
        Map<String,String> sqlMap = Maps.newHashMap();
        StringBuilder querySql = new StringBuilder("select ");
        String select = null;
        Set<String> s2 = new TreeSet<>();
        for (Table table : statement.getTables()) {
            TableInfo tableInfo = tableList.getTable(table.getName());
            if(ListUtil.isNotEmpty(tableInfo.getPrimaryKeys())){
                //TODO sql中没有别名的情况
                if(StringUtils.isBlank(select)){
                    select = table.getAlias().getName().concat(".").concat(tableInfo.getPrimaryKeys().get(0));
                    sqlMap.put("primaryKey",tableInfo.getPrimaryKeys().get(0));

                }
                String tableName = table.getName().concat(" ").concat(table.getAlias().getName());
                s2.add(tableName);
            }
        }
        String from = s2.stream().collect(Collectors.joining(","));
        querySql.append(select).append(" from ").append(from).append(" where ").append(statement.getWhere().toString());
        log.info("update Sql Analyse =[{}]",querySql.toString());
        sqlMap.put("select",select);
        sqlMap.put("querySql",querySql.toString());
        return sqlMap;
    }


    private Map<String,String> updateSqlAnalyseSingleTable(TableList tableList, Update statement,String sql) {
        Map<String,String> sqlMap = Maps.newHashMap();
        //单表操作
        Table table = statement.getTables().get(0);
        String querySql = "select ";
        TableInfo tableInfo = tableList.getTable(table.getName());
        String primaryKey = tableInfo.getPrimaryKeys().get(0);
        String select = "";
        if(null != table.getAlias()){
            select = table.getAlias().getName().concat(".").concat(primaryKey);
        }
        querySql = querySql.concat(select).concat(statement.getWhere().toString());
        log.info("update Sql Analyse =[{}]",querySql);
        sqlMap.put("primaryKey",primaryKey);
        sqlMap.put("select",select);
        sqlMap.put("querySql",querySql);
        return sqlMap;
    }

    private  boolean checkTableContainsPk(Update statement, TableList tableList ) {
        boolean containsPk = false;
        for (Table table : statement.getTables()) {
            TableInfo tableInfo = tableList.getTable(table.getName());
            if(ListUtil.isNotEmpty(tableInfo.getPrimaryKeys())){
                containsPk =true;
            }
        }
        return containsPk;
    }

}
