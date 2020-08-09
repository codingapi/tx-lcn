package com.codingapi.txlcn.tc.jdbc.database;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.JDBCType;

/**
 * @author lorne 2020/8/9
 */
@Data
@NoArgsConstructor
public class ColumnInfo {

    private String name;
    private JDBCType type;
    private boolean primaryKey;

    public ColumnInfo(String name, JDBCType type, boolean primaryKey) {
        this.name = name;
        this.type = type;
        this.primaryKey = primaryKey;
    }
}
