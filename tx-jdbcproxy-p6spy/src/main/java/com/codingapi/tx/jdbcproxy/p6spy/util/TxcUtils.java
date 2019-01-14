package com.codingapi.tx.jdbcproxy.p6spy.util;

/**
 * Description:
 * Date: 19-1-14 上午10:19
 *
 * @author ujued
 */
public class TxcUtils {
    public static String txcSQL(String protoSQL) {
        return protoSQL + "_lcn";
    }

    public static String protoSQL(String txcSQL) {
        return txcSQL.substring(0, txcSQL.length() - 4);
    }

    public static boolean isTxcSQL(String sql) {
        return sql.endsWith("_lcn");
    }
}
