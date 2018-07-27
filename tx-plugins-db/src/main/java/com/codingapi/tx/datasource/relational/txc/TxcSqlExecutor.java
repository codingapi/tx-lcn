package com.codingapi.tx.datasource.relational.txc;


import com.codingapi.tx.datasource.relational.txc.parser.ResultConvertUtils;
import com.codingapi.tx.datasource.relational.txc.parser.SQLType;
import com.codingapi.tx.datasource.relational.txc.parser.TxcLine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jsy.
 * @title
 * @time 17/12/26.
 */
public class TxcSqlExecutor {

    public static List<TxcLine> executeQuery(String sql, Connection connection) throws SQLException {
        ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
        return ResultConvertUtils.convertWithPrimary(resultSet, null, SQLType.SELECT);
    }

    public static List<TxcLine> executeQuery(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        return ResultConvertUtils.convertWithPrimary(resultSet, null, SQLType.SELECT);
    }

    public static void execute(String sql, Connection connection) throws SQLException {
        connection.prepareStatement(sql).execute();
    }
}
