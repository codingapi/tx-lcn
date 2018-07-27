package com.codingapi.tx.datasource.relational.txc.parser;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;

import java.util.List;

/**
 * @author jsy.
 * @title
 * @time 17/12/22.
 */
public class DeleteParser extends AbstractParser<MySqlDeleteStatement> {
    private static DeleteParser instance = null;

    public static DeleteParser getInstance() {
        if (instance == null) {
            synchronized (DeleteParser.class) {
                if (instance == null) {
                    instance = new DeleteParser();
                }
            }
        }
        return instance;
    }

    @Override
    protected List<Object> getWhereParams(List<Object> sqlParamsList, MySqlDeleteStatement parseSqlStatement) {
        if (CollectionUtils.isNotEmpty(sqlParamsList)) {
            return sqlParamsList;
        }
        return ListUtils.EMPTY_LIST;
    }

    @Override
    protected String getWhere(MySqlDeleteStatement parseSqlStatement) {
        return SqlUtils.toSQLString(parseSqlStatement.getWhere());
    }

    @Override
    public TxcTable getPresentValue(List<Object> sqlParamsList, MySqlDeleteStatement parseSqlStatement) {
        return null;
    }


    @Override
    public SQLType getSqlType() {
        return SQLType.DELETE;
    }

    @Override
    protected String selectSql(MySqlDeleteStatement mySqlUpdateStatement, String primaryKeyName) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * ");

        //
        stringBuffer.append(" from ").append(mySqlUpdateStatement.getTableName().getSimpleName()).append(" where ");
        //
        stringBuffer.append(SqlUtils.toSQLString(mySqlUpdateStatement.getWhere()));
        return stringBuffer.toString();
    }

    @Override
    protected String getTableName(MySqlDeleteStatement parseSqlStatement) {
        return parseSqlStatement.getTableName().getSimpleName();
    }



}
