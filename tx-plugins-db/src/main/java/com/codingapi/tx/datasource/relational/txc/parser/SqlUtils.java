package com.codingapi.tx.datasource.relational.txc.parser;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;

/**
 * @author jsy.
 * @title
 * @time 17/12/15.
 */
public class SqlUtils {

    private static final String dbType = "mysql";

    public static String toSQLString(SQLObject sqlObject) {
        return SQLUtils.toSQLString(sqlObject);
    }
}
