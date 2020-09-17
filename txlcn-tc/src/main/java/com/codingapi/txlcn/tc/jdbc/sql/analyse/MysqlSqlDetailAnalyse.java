package com.codingapi.txlcn.tc.jdbc.sql.analyse;

import com.codingapi.txlcn.tc.jdbc.database.SqlAnalyseInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Join;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
public class MysqlSqlDetailAnalyse implements SqlDetailAnalyse{

    @Override
    public boolean singleInsertAnalyse(String sql, Connection connection, Insert statement, ItemsList itemsList, String pk, int pkIndex) throws SQLException {
        if (itemsList instanceof ExpressionList) {
            ExpressionList expressionList = (ExpressionList) itemsList;
            if(pkIndex > -1){
                String defaultValue = expressionList.getExpressions().get(pkIndex).toString();
                if(!"NULL".equals(defaultValue.toUpperCase())){
                    return true;
                }
                expressionList.getExpressions().remove(pkIndex);
                statement.getColumns().remove(pkIndex);
            }
            QueryRunner queryRunner = new QueryRunner();
            List<Map<String, Object>> insert = queryRunner.insert(connection, sql, new MapListHandler());

            statement.getColumns().add(new Column(pk));
            expressionList.getExpressions().add(new StringValue(insert.get(0).get("GENERATED_KEY").toString()));
        }
        return false;
    }

    @Override
    public boolean multiInsertAnalyse(String sql, Connection connection, Insert statement, ItemsList itemsList, String pk, int pkIndex) throws SQLException {
        if (itemsList instanceof MultiExpressionList) {
            MultiExpressionList multiExpressionList = (MultiExpressionList) itemsList;
            if(pkIndex > -1){
                for (ExpressionList expressionList : multiExpressionList.getExprList()) {
                    String defaultValue = expressionList.getExpressions().get(pkIndex).toString();
                    if(!"NULL".equals(defaultValue.toUpperCase())){
                        return true;
                    }
                    expressionList.getExpressions().remove(pkIndex);
                    statement.getColumns().remove(pkIndex);
                }
            }
            QueryRunner queryRunner = new QueryRunner();
            List<Map<String, Object>> insert = queryRunner.insert(connection, sql, new MapListHandler());

            statement.getColumns().add(new Column(pk));
            for (int i = 0; i < multiExpressionList.getExprList().size(); i++) {
                multiExpressionList.getExprList().get(i).getExpressions().add(new StringValue(insert.get(i).get("GENERATED_KEY").toString()));
            }
        }
        return false;
    }

    @Override
    public SqlAnalyseInfo sqlAnalyseSingleTable(TableList tableList, Table table, Expression where, List<Join> joinList) {
        SqlAnalyseInfo sqlAnalyseInfo = new SqlAnalyseInfo();
        //单表操作
        StringBuilder querySql = new StringBuilder("select ");
        TableInfo tableInfo = tableList.getTable(table.getName());
        String primaryKey = tableInfo.getPrimaryKeys().get(0);
        String select = "";
        StringBuilder tableName = new StringBuilder();
        if(null != table.getAlias()){
            select = table.getAlias().getName().concat(".").concat(primaryKey);
            tableName.append(table.getName()).append(" ").append(table.getAlias());
        }else {
            select = primaryKey;
            tableName.append(table.getName());
        }

        for (Join join : joinList) {
            Table table1 = (Table)join.getRightItem();
            if(table1.getAlias()!=null){
                tableName.append(",").append(table1.getName()).append(" ").append(table1.getAlias());
            }else {
                tableName.append(",").append(table1.getName());
            }
        }

        querySql = querySql.append(select).append(" from ").append(tableName).append(" where ").append(where.toString());
        log.info("update Sql Analyse =[{}]",querySql);
        JDBCType type = tableInfo.getColumnInfos().stream().filter(m -> m.getName().equals(primaryKey)).findFirst().get().getType();

        sqlAnalyseInfo.setSelect(select);
        sqlAnalyseInfo.setQuerySql(querySql.toString());
        sqlAnalyseInfo.setPrimaryKey(primaryKey);
        sqlAnalyseInfo.setPrimaryKeyType(type);
        return sqlAnalyseInfo;
    }

    @Override
    public String splicingNewSql(String sql, SqlAnalyseInfo sqlAnalyseInfo, List<Map<String, Object>> query) {
        String select = sqlAnalyseInfo.getSelect();
        String primaryKey = sqlAnalyseInfo.getPrimaryKey();
        JDBCType primaryKeyType = sqlAnalyseInfo.getPrimaryKeyType();
        sql = sql.concat(" and ").concat(select).concat(" in ( ");
        int size = query.size();
        for (int i = 0; i < query.size(); i++) {
            if(JDBCType.VARCHAR.getName().equals(primaryKeyType.getName())
                    ||JDBCType.NVARCHAR.getName().equals(primaryKeyType.getName())
                    ||JDBCType.CHAR.getName().equals(primaryKeyType.getName())

            ){
                sql  = sql.concat("'").concat((String) query.get(i).get(primaryKey)).concat("'");
            }
            else   if(JDBCType.INTEGER.getName().equals(primaryKeyType.getName())
                    ||JDBCType.SMALLINT.getName().equals(primaryKeyType.getName())
                    ||JDBCType.TINYINT.getName().equals(primaryKeyType.getName())
            ){
                sql  = sql.concat(Integer.toString((Integer) query.get(i).get(primaryKey)));
            }
            else   if(JDBCType.BIGINT.getName().equals(primaryKeyType.getName())){
                sql  = sql.concat(Long.toString((Long) query.get(i).get(primaryKey)));
            }else{
                sql  = sql.concat("'").concat((String) query.get(i).get(primaryKey)).concat("'");
            }

            if(i + 1 < size){
                sql = sql.concat(" , ");
            }

        }
        sql = sql.concat(")");
        return sql;
    }
}
