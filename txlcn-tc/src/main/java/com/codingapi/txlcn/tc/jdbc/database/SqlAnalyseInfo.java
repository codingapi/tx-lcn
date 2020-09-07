package com.codingapi.txlcn.tc.jdbc.database;

import lombok.Data;

import java.sql.JDBCType;

/**
 * @author Gz.
 * @description:
 * @date 2020-08-15 15:57:00
 */
@Data
public class SqlAnalyseInfo {

    private String select;

    private String querySql;

    private String primaryKey;

    private JDBCType primaryKeyType;
}
