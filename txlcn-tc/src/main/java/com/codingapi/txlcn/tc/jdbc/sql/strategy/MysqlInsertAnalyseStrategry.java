package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.JdbcAnalyseUtils;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * @author Gz.
 * @description: insert 语句分析
 * @date 2020-08-13 23:08:26
 */
@Slf4j
public class MysqlInsertAnalyseStrategry implements MysqlSqlAnalyseStrategry {


    @Override
    public String MysqlAnalyseStrategry(String sql, Connection connection) throws SQLException, JSQLParserException {
        boolean defaultAutoCommit = connection.getAutoCommit();
        connection.setAutoCommit(false);
        String catalog = connection.getCatalog();
        DataBaseContext.getInstance().push(catalog, JdbcAnalyseUtils.analyse(connection));
        TableList tableList =  DataBaseContext.getInstance().get(catalog);
        Insert statement = (Insert) CCJSqlParserUtil.parse(sql);
        Table table = statement.getTable();
        ItemsList itemsList = statement.getItemsList();

        if(!SqlAnalyseHelper.checkTableContainsPk(table, tableList)){
            return sql;
        }
        TableInfo tableInfo = tableList.getTable(table.getName());
        String pk = tableInfo.getPrimaryKeys().get(0);

        int pkIndex = 0;
        boolean contains = false;
        for (int i = 0; i < statement.getColumns().size(); i++) {
            if(statement.getColumns().get(i).getColumnName().equals(pk)){
                pkIndex = i;
                contains = true;
                break;
            }
        }


        if (itemsList instanceof MultiExpressionList) {
            MultiExpressionList multiExpressionList = (MultiExpressionList) itemsList;
            if(contains){
                for (ExpressionList expressionList : multiExpressionList.getExprList()) {
                    String defaultValue = expressionList.getExpressions().get(pkIndex).toString();
                    if(!"NULL".equals(defaultValue.toUpperCase())){
                        return sql;
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
        if (itemsList instanceof ExpressionList) {
            ExpressionList expressionList = (ExpressionList) itemsList;

            if(contains){
                String defaultValue = expressionList.getExpressions().get(pkIndex).toString();
                if(!"NULL".equals(defaultValue.toUpperCase())){
                    return sql;
                }
                expressionList.getExpressions().remove(pkIndex);
                statement.getColumns().remove(pkIndex);
            }

            QueryRunner queryRunner = new QueryRunner();
            List<Map<String, Object>> insert = queryRunner.insert(connection, sql, new MapListHandler());

            statement.getColumns().add(new Column(pk));
            expressionList.getExpressions().add(new StringValue(insert.toString()));
        }




        connection.rollback();
        connection.setAutoCommit(defaultAutoCommit);
        log.info("newSql=[{}]",statement.toString());
        return statement.toString();
    }
}
