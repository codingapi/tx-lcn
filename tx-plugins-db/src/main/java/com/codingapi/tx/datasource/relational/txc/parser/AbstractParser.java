package com.codingapi.tx.datasource.relational.txc.parser;

import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.codingapi.tx.datasource.relational.txc.TableMetaUtils;
import com.codingapi.tx.datasource.relational.txc.TxcPreparedStatement;
import com.codingapi.tx.datasource.relational.txc.TxcStatement;
import org.apache.commons.collections.CollectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author jsy.
 * @title
 * @time 17/12/11.
 */

public abstract class AbstractParser<T>{

    public CommitInfo parse(TxcStatement txcStatement) throws SQLException {
        CommitInfo commitInfo = new CommitInfo();

        String sql = txcStatement.getSql();
        T sqlParseStatement = (T) new MySqlStatementParser(sql).parseStatement();

        //设置sqltype
        commitInfo.setSqlType(getSqlType());

        //设置where
        commitInfo.setWhere(getWhere(sqlParseStatement));

        //sql
        commitInfo.setSql(sql);



        if (txcStatement instanceof TxcPreparedStatement) {
            commitInfo.setSqlParams(((TxcPreparedStatement) txcStatement).getParamsList());
            commitInfo.setWhereParams(
                    getWhereParams(((TxcPreparedStatement) txcStatement).getParamsList(), sqlParseStatement));
        }

        //解析之前的值
        commitInfo.setOriginalValue(getOriginValue(commitInfo.getWhereParams(), sqlParseStatement, txcStatement.getConnection()));

        //解析之后的值
        commitInfo.setPresentValue(getPresentValue(commitInfo.getSqlParams(), sqlParseStatement));

        return commitInfo;



    }

    protected abstract List<Object> getWhereParams(List<Object> sqlParamsList, T parseSqlStatement);

    protected abstract String getWhere(T parseSqlStatement);

    //从当前sql取出值
    public abstract TxcTable getPresentValue(List<Object> sqlParamsList, T parseSqlStatement);

    //从数据库取出值
    public  TxcTable getOriginValue(List<Object> whereParamsList, T parseSqlStatement, Connection connection)
            throws SQLException {
        TxcTable txcTable = new TxcTable();
        txcTable.setTableName(getTableName(parseSqlStatement));

        // 组装sql
        String primaryKeyName = TableMetaUtils
                .getTableMetaInfo(connection, getTableName(parseSqlStatement)).getPrimaryKeyName();
        String selectSql = selectSql(parseSqlStatement, primaryKeyName);

        PreparedStatement preparedStatement = connection.prepareStatement(selectSql);

        if (CollectionUtils.isNotEmpty(whereParamsList)) {
            // 设置条件
            for (int i = 1; i <= whereParamsList.size(); i++) {
                preparedStatement.setObject(i, whereParamsList.get(i - 1));
            }
        }

        // 执行查询sql
        ResultSet resultSet = preparedStatement.executeQuery();

        List<TxcLine> txcLines = ResultConvertUtils.convertWithPrimary(resultSet, primaryKeyName, getSqlType());

        // convert
        txcTable.setLine(txcLines);
        return txcTable;
    }


    public abstract SQLType getSqlType();



//
//    public int getTypeByClass(Object x) {
//        if (x == null) {
//            return Types.OTHER;
//        }
//
//        Class<?> clazz = x.getClass();
//        if (clazz == Byte.class) {
//            return Types.TINYINT;
//        }
//
//        if (clazz == Short.class) {
//            return Types.SMALLINT;
//        }
//
//        if (clazz == Integer.class) {
//            return Types.INTEGER;
//        }
//
//        if (clazz == Long.class) {
//
//            return Types.BIGINT;
//        }
//
//        if (clazz == String.class) {
//            return Types.VARCHAR;
//        }
//
//        if (clazz == BigDecimal.class) {
//            return Types.DECIMAL;
//        }
//
//        if (clazz == Float.class) {
//            return Types.FLOAT;
//        }
//
//        if (clazz == Double.class) {
//            return Types.DOUBLE;
//        }
//
//        if (clazz == java.sql.Date.class || clazz == java.util.Date.class) {
//            return Types.DATE;
//        }
//
//        if (clazz == java.sql.Timestamp.class) {
//            return Types.TIMESTAMP;
//        }
//
//        if (clazz == java.sql.Time.class) {
//            return Types.TIME;
//        }
//
//        if (clazz == Boolean.class) {
//            return Types.BOOLEAN;
//        }
//
//        if (clazz == byte[].class) {
//            return JdbcParameter.TYPE.BYTES;
//        }
//
//        if (x instanceof InputStream) {
//            return JdbcParameter.TYPE.BinaryInputStream;
//        }
//
//        if (x instanceof Reader) {
//            return JdbcParameter.TYPE.CharacterInputStream;
//        }
//
//        if (x instanceof Clob) {
//            return Types.CLOB;
//        }
//
//        if (x instanceof NClob) {
//            return Types.NCLOB;
//        }
//
//        if (x instanceof Blob) {
//            return Types.BLOB;
//        }
//        return Types.OTHER;
//
//    }

    protected abstract String selectSql(T parseSqlStatement, String primaryKeyName);

    protected abstract String getTableName(T parseSqlStatement);

}
