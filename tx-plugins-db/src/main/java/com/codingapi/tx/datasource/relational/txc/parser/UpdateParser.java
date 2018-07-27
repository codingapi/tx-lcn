package com.codingapi.tx.datasource.relational.txc.parser;

import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author jsy.
 * @title
 * @time 17/12/11.
 */
public class UpdateParser extends AbstractParser<SQLUpdateStatement> {
    private static final Logger logger = LoggerFactory.getLogger(UpdateParser.class);


    private static UpdateParser instance = null;

    public static UpdateParser getInstance() {
        if (instance == null) {
            synchronized (UpdateParser.class) {
                if (instance == null) {
                    instance = new UpdateParser();
                }
            }
        }
        return instance;
    }

    @Override
    protected List<Object> getWhereParams(List<Object> sqlParamsList, SQLUpdateStatement parseSqlStatement) {
        if (CollectionUtils.isNotEmpty(sqlParamsList)) {
            int size = 0;

            for (SQLUpdateSetItem sqlUpdateSetItem :parseSqlStatement.getItems()) {
                if (sqlUpdateSetItem.getValue() instanceof SQLVariantRefExpr) {
                    size++;
                }
            }
            return sqlParamsList.subList(size, sqlParamsList.size());
        }
        return ListUtils.EMPTY_LIST;

    }


    @Override
    protected String getWhere(SQLUpdateStatement parseSqlStatement) {
        return SqlUtils.toSQLString(parseSqlStatement.getWhere());
    }

    @Override
    public TxcTable getPresentValue(List<Object> sqlParamsList, SQLUpdateStatement parseSqlStatement) {

        TxcTable txcTable = new TxcTable();
        txcTable.setTableName(parseSqlStatement.getTableName().getSimpleName());

        TxcLine txcLine = new TxcLine();
        List<SQLUpdateSetItem> items = parseSqlStatement.getItems();

        int variantExpr = 0;
        for (int i = 0; i < items.size(); i++) {
            SQLUpdateSetItem sqlUpdateSetItem = items.get(i);
            TxcField txcField = new TxcField();
            String cloumnName = SqlUtils.toSQLString(sqlUpdateSetItem.getColumn()).replace("\'", "").replace("`", "").trim();
            txcField.setName(cloumnName);
            if (sqlUpdateSetItem.getValue() instanceof SQLVariantRefExpr) {
                txcField.setValue(sqlParamsList.get(variantExpr++));
            }  else if (sqlUpdateSetItem.getValue() instanceof SQLValuableExpr){
                txcField.setValue(SqlUtils.toSQLString(items.get(i).getValue()));
            } else {
                logger.info("不支持复杂的sql,{}", sqlUpdateSetItem.getClass().toString());
                throw new RuntimeException("不支持复杂的sql");
            }

            txcLine.getFields().add(txcField);
        }
        txcTable.getLine().add(txcLine);

        return txcTable;
    }




    @Override
    public SQLType getSqlType() {
        return SQLType.UPDATE;
    }


    @Override
    protected String selectSql(SQLUpdateStatement mySqlUpdateStatement, String primaryKeyName) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT ");


        List<SQLUpdateSetItem> items = mySqlUpdateStatement.getItems();
        for (SQLUpdateSetItem sqlUpdateSetItem : items) {

            stringBuffer.append(SqlUtils.toSQLString(sqlUpdateSetItem.getColumn())).append(",");

        }

        stringBuffer.append(primaryKeyName);



        stringBuffer.append(" from ").append(mySqlUpdateStatement.getTableName().getSimpleName()).append(" where ");

        stringBuffer.append(SqlUtils.toSQLString(mySqlUpdateStatement.getWhere()));
        return stringBuffer.toString();
    }

    @Override
    protected String getTableName(SQLUpdateStatement parseSqlStatement) {
        return parseSqlStatement.getTableName().getSimpleName();
    }

}
