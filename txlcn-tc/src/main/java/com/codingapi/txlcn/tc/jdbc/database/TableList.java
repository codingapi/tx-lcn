package com.codingapi.txlcn.tc.jdbc.database;

import lombok.Data;

import java.util.List;

/**
 * @author lorne 2020/8/9
 */
@Data
public class TableList {

    private List<TableInfo> tableInfos;


    public TableList(List<TableInfo> tableInfos) {
        this.tableInfos = tableInfos;
    }

    public boolean isEmpty() {
        return tableInfos==null||tableInfos.size()==0;
    }


    public TableInfo getTable(String tableName){
        for(TableInfo tableInfo:tableInfos){
            if(tableInfo.getTable().equalsIgnoreCase(tableName)){
                return tableInfo;
            }
        }
        return null;
    }
}
