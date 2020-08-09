package com.codingapi.txlcn.tc.jdbc.database;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lorne 2020/8/9
 */
@Data
public class TableInfo {

    private String table;

    private List<ColumnInfo> columnInfos;

    private List<String> primaryKeys;

    public TableInfo() {
        this.columnInfos = new ArrayList<>();
        this.primaryKeys = new ArrayList<>();
    }
}
