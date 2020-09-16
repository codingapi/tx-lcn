package com.codingapi.txlcn.tc.jdbc.database;

import com.codingapi.txlcn.tc.utils.ListUtil;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Join;

import java.sql.JDBCType;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Gz.
 * @description: sql分析助手类
 * @date 2020-08-17 23:26:01
 */
@Slf4j
public class SqlAnalyseHelper {

    /**
     * 检查是否包含主键
     * @param table
     * @param tableList
     * @return
     */
    public static  boolean checkTableContainsPk(Table table, TableList tableList ) {
        TableInfo tableInfo = tableList.getTable(table.getName());
        if(ListUtil.isNotEmpty(tableInfo.getPrimaryKeys())){
            return  true;
        }
        return false;
    }
    public static boolean checkWhereContainsPk(Table table,TableList tableList,String whereSql){
        TableInfo tableInfo = tableList.getTable(table.getName());
        return  whereSql.toUpperCase().contains(tableInfo.getPrimaryKeys().get(0).toUpperCase());
    }



//    public static SqlAnalyseInfo sqlAnalyseSingleTable(TableList tableList, Table table, Expression where, List<Join> joinList) {
//        SqlAnalyseInfo sqlAnalyseInfo = new SqlAnalyseInfo();
//        //单表操作
//        StringBuilder querySql = new StringBuilder("select ");
//        TableInfo tableInfo = tableList.getTable(table.getName());
//        String primaryKey = tableInfo.getPrimaryKeys().get(0);
//        String select = "";
//        StringBuilder tableName = new StringBuilder();
//        if(null != table.getAlias()){
//            select = table.getAlias().getName().concat(".").concat(primaryKey);
//            tableName.append(table.getName()).append(" ").append(table.getAlias());
//        }else {
//            select = primaryKey;
//            tableName.append(table.getName());
//        }
//
//        for (Join join : joinList) {
//            Table table1 = (Table)join.getRightItem();
//            if(table1.getAlias()!=null){
//                tableName.append(",").append(table1.getName()).append(" ").append(table1.getAlias());
//            }else {
//                tableName.append(",").append(table1.getName());
//            }
//        }
//
//        querySql = querySql.append(select).append(" from ").append(tableName).append(" where ").append(where.toString());
//        log.info("update Sql Analyse =[{}]",querySql);
//        JDBCType type = tableInfo.getColumnInfos().stream().filter(m -> m.getName().equals(primaryKey)).findFirst().get().getType();
//
//        sqlAnalyseInfo.setSelect(select);
//        sqlAnalyseInfo.setQuerySql(querySql.toString());
//        sqlAnalyseInfo.setPrimaryKey(primaryKey);
//        sqlAnalyseInfo.setPrimaryKeyType(type);
//        return sqlAnalyseInfo;
//    }


//    public static SqlAnalyseInfo sqlAnalyseMultiTable(TableList tableList, Table table, Expression where) {
//        SqlAnalyseInfo sqlAnalyseInfo = new SqlAnalyseInfo();
//
//        StringBuilder querySql = new StringBuilder("select ");
//        String select = null;
//        Set<String> s2 = new TreeSet<>();
//
//        TableInfo tableInfo = tableList.getTable(table.getName());
//        if(ListUtil.isNotEmpty(tableInfo.getPrimaryKeys())){
//            //TODO sql中没有别名的情况
//            if(StringUtils.isBlank(select)){
//                select = table.getAlias().getName().concat(".").concat(tableInfo.getPrimaryKeys().get(0));
//                String pk = tableInfo.getPrimaryKeys().get(0);
//                JDBCType type = tableInfo.getColumnInfos().stream().filter(m -> m.getName().equals(pk)).findFirst().get().getType();
//                sqlAnalyseInfo.setPrimaryKey(pk);
//                sqlAnalyseInfo.setPrimaryKeyType(type);
//            }
//            String tableName = table.getName().concat(" ").concat(table.getAlias().getName());
//            s2.add(tableName);
//        }
//
//        String from = s2.stream().collect(Collectors.joining(","));
//        querySql.append(select).append(" from ").append(from).append(" where ").append(where.toString());
//        log.info("update Sql Analyse =[{}]",querySql.toString());
//
//        sqlAnalyseInfo.setSelect(select);
//        sqlAnalyseInfo.setQuerySql(querySql.toString());
//
//        return sqlAnalyseInfo;
//    }

//    public static String getNewSql(String sql, SqlAnalyseInfo sqlAnalyseInfo, List<Map<String, Object>> query) {
//        String select = sqlAnalyseInfo.getSelect();
//        String primaryKey = sqlAnalyseInfo.getPrimaryKey();
//        JDBCType primaryKeyType = sqlAnalyseInfo.getPrimaryKeyType();
//        sql = sql.concat(" and ").concat(select).concat(" in ( ");
//        int size = query.size();
//        for (int i = 0; i < query.size(); i++) {
//            if(JDBCType.VARCHAR.getName().equals(primaryKeyType.getName())
//                    ||JDBCType.NVARCHAR.getName().equals(primaryKeyType.getName())
//                    ||JDBCType.CHAR.getName().equals(primaryKeyType.getName())
//
//            ){
//                sql  = sql.concat("'").concat((String) query.get(i).get(primaryKey)).concat("'");
//            }
//            else   if(JDBCType.INTEGER.getName().equals(primaryKeyType.getName())
//                    ||JDBCType.SMALLINT.getName().equals(primaryKeyType.getName())
//                    ||JDBCType.TINYINT.getName().equals(primaryKeyType.getName())
//            ){
//                sql  = sql.concat(Integer.toString((Integer) query.get(i).get(primaryKey)));
//            }
//            else   if(JDBCType.BIGINT.getName().equals(primaryKeyType.getName())){
//                sql  = sql.concat(Long.toString((Long) query.get(i).get(primaryKey)));
//            }else{
//                sql  = sql.concat("'").concat((String) query.get(i).get(primaryKey)).concat("'");
//            }
//
//            if(i + 1 < size){
//                sql = sql.concat(" , ");
//            }
//
//        }
//        sql = sql.concat(")");
//        return sql;
//    }
}
