package com.codingapi.txlcn.tc.jdbc.sql.analyse;

import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.insert.Insert;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MysqlAnalyse  implements SqlAnalyse{
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
            expressionList.getExpressions().add(new StringValue(insert.toString()));
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
}
