package com.codingapi.txlcn.tc.jdbc.database;

import org.apache.commons.dbutils.DbUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lorne 2020/8/9
 */
public class JdbcAnalyseUtils {

    public static List<TableInfo> analyse(Connection connection) throws SQLException {

        List<TableInfo> tableInfos = new ArrayList<>();
        String catalog = connection.getCatalog();
        DatabaseMetaData databaseMetaData =  connection.getMetaData();

        ResultSet schemas =  databaseMetaData.getTables(catalog,"%","%",new String[]{"TABLE"});
        while (schemas.next()){
            TableInfo tableInfo= new TableInfo();

            String tableName = schemas.getString("TABLE_NAME");
            tableInfo.setTable(tableName);

            ResultSet colRet = databaseMetaData.getColumns(catalog,"%", tableName,"%");
            while(colRet.next()) {
                int dataType = colRet.getInt("DATA_TYPE");
                String columnName = colRet.getString("COLUMN_NAME");
                String isAutoincrement = colRet.getString("IS_AUTOINCREMENT");
                tableInfo.getColumnInfos().add(new ColumnInfo(columnName,JDBCType.valueOf(dataType),"YES".equalsIgnoreCase(isAutoincrement)));
            }
            DbUtils.close(colRet);
            ResultSet primaryKeyResultSet =  databaseMetaData.getPrimaryKeys(catalog,null,tableName);
            while (primaryKeyResultSet.next()){
                tableInfo.getPrimaryKeys().add(primaryKeyResultSet.getString("COLUMN_NAME"));
            }
            DbUtils.close(primaryKeyResultSet);
            tableInfos.add(tableInfo);
        }
        DbUtils.close(schemas);
        return tableInfos;
    }
}
