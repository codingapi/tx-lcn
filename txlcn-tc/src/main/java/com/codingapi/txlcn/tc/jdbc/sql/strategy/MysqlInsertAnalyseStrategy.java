package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.SqlAnalyseHelper;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import com.codingapi.txlcn.tc.jdbc.sql.analyse.MysqlAnalyse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * @author Gz.
 * @description: insert 语句分析
 * @date 2020-08-13 23:08:26
 */
@Slf4j
public class  MysqlInsertAnalyseStrategy implements SqlSqlAnalyseHandler {


    private MysqlAnalyse mysqlAnalyse;

    @Override
    public String mysqlAnalyseStrategy(String sql, Connection connection,Statement stmt) throws SQLException, JSQLParserException {
        boolean defaultAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String catalog = connection.getCatalog();
        TableList tableList =  DataBaseContext.getInstance().get(catalog);
        Insert statement = (Insert) stmt;
        Table table = statement.getTable();
        ItemsList itemsList = statement.getItemsList();

        if(null == itemsList){
            return sql;
        }
        if(!SqlAnalyseHelper.checkTableContainsPk(table, tableList)){
            return sql;
        }
        TableInfo tableInfo = tableList.getTable(table.getName());
        String pk = tableInfo.getPrimaryKeys().get(0);

        int pkIndex = -1;
        for (int i = 0; i < statement.getColumns().size(); i++) {
            if(statement.getColumns().get(i).getColumnName().toUpperCase().equals(pk.toUpperCase())){
                pkIndex = i;
                break;
            }
        }

        if (mysqlAnalyse.multiInsertAnalyse(sql, connection, statement, itemsList, pk, pkIndex)) return sql;

        if (mysqlAnalyse.singleInsertAnalyse(sql, connection, statement, itemsList, pk, pkIndex)) return sql;

        connection.rollback();
        connection.setAutoCommit(defaultAutoCommit);
        log.info("newSql=[{}]",statement.toString());
        return statement.toString();
    }

    @Override
    public void afterPropertiesSet()  {
        AnalyseStrategryFactory.register(MysqlAnalyseEnum.INSERT.name(), this);
    }
}
