package com.codingapi.txlcn.tc.jdbc.sql.strategy.mysql;

import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import com.codingapi.txlcn.tc.jdbc.sql.analyse.SqlDetailAnalyse;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.SqlDetailAnalyseFactory;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.SqlSqlAnalyseHandler;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.chan.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * @author Gz.
 * @description: insert 语句分析
 * @date 2020-08-13 23:08:26
 */
@Slf4j
public class  MysqlInsertAnalyseStrategy implements SqlSqlAnalyseHandler {

    private SqlDetailAnalyseFactory sqlDetailAnalyseFactory;

    public MysqlInsertAnalyseStrategy(SqlDetailAnalyseFactory sqlDetailAnalyseFactory){
        this.sqlDetailAnalyseFactory = sqlDetailAnalyseFactory;
    }


    @Override
    public String analyse(String sqlType, String sql, Connection connection, Statement stmt) throws SQLException, JSQLParserException {
        boolean defaultAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String catalog = connection.getCatalog();
        TableList tableList =  DataBaseContext.getInstance().get(catalog);
        Insert statement = (Insert) stmt;
        Table table = statement.getTable();
        ItemsList itemsList = statement.getItemsList();

        FilterFacade filterFacade = FilterFacade.builder().tableList(tableList).table(table).itemsList(itemsList).build();
        SqlAnalysqFilterChain filter = new SqlAnalysqFilterChain();
        filter.add(new CheckTableContainsPkFilter()).add(new ItemsListFilter());
        if(!filter.doFilter(filterFacade)){
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

        SqlDetailAnalyse invokeStrategy = sqlDetailAnalyseFactory.getInvokeStrategy(sqlType);
        if (invokeStrategy.multiInsertAnalyse(sql, connection, statement, itemsList, pk, pkIndex)) return sql;

        if (invokeStrategy.singleInsertAnalyse(sql, connection, statement, itemsList, pk, pkIndex)) return sql;

        connection.rollback();
        connection.setAutoCommit(defaultAutoCommit);
        log.info("newSql=[{}]",statement.toString());
        return statement.toString();
    }

    @Override
    public boolean preAnalyse(String sqlType,Statement stmt) {
        return "mysql".equals(sqlType)&&stmt instanceof Insert;
    }

}
