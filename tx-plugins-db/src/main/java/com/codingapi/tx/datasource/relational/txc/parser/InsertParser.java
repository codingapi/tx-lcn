package com.codingapi.tx.datasource.relational.txc.parser;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import org.apache.commons.collections.CollectionUtils;

import java.sql.Connection;
import java.util.List;

/**
 * @author jsy.
 * @title
 * @time 17/12/22.
 */
public class InsertParser extends AbstractParser<MySqlInsertStatement>{

    private static InsertParser instance = null;

    public static InsertParser getInstance() {
        if (instance == null) {
            synchronized (InsertParser.class) {
                if (instance == null) {
                    instance = new InsertParser();
                }
            }
        }
        return instance;
    }
    @Override
    protected List<Object> getWhereParams(List<Object> sqlParamsList,
                                          MySqlInsertStatement parseSqlStatement) {
        return null;
    }

    @Override
    protected String getWhere(MySqlInsertStatement parseSqlStatement) {
        return null;
    }

    @Override
    public TxcTable getPresentValue(List<Object> sqlParamsList, MySqlInsertStatement parseSqlStatement) {

        TxcTable txcTable = new TxcTable();
        txcTable.setTableName(parseSqlStatement.getTableName().getSimpleName());
        List<TxcLine> line = txcTable.getLine();

        List<SQLInsertStatement.ValuesClause> valuesList = parseSqlStatement.getValuesList();
        List<SQLExpr> columns = parseSqlStatement.getColumns();


        for (SQLInsertStatement.ValuesClause valuesClause : valuesList) {
            List<SQLExpr> values = valuesClause.getValues();
            TxcLine txcLine = new TxcLine();
            for (int i = 0; i < columns.size(); i++) {
                TxcField txcField = new TxcField();
                txcField.setName(SqlUtils.toSQLString(columns.get(i)).replace("\'", "").replace("`", "").trim());
                if (CollectionUtils.isNotEmpty(sqlParamsList)) {
                    txcField.setValue(sqlParamsList.get(i));
                } else {
                    txcField.setValue(SqlUtils.toSQLString(values.get(i)));
                }
                txcLine.getFields().add(txcField);
            }
            line.add(txcLine);
        }



        return txcTable;
    }

    @Override
    public TxcTable getOriginValue(List<Object> whereParamsList, MySqlInsertStatement parseSqlStatement,
                                   Connection connection) {
        return null;
    }


    @Override
    public SQLType getSqlType() {
        return SQLType.INSERT;
    }

    @Override
    protected String selectSql(MySqlInsertStatement parseSqlStatement, String primaryKeyName) {
        throw new RuntimeException("不支持的类型");
    }

    @Override
    protected String getTableName(MySqlInsertStatement parseSqlStatement) {
        return parseSqlStatement.getTableName().getSimpleName();
    }
}
