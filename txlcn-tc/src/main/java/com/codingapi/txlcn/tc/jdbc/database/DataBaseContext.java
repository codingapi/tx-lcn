package com.codingapi.txlcn.tc.jdbc.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分析数据库表的信息
 *
 * @author lorne 2020/8/9
 *
 */
public class DataBaseContext {

    private Map<String,List<TableInfo>> map;

    private static DataBaseContext context;

    private DataBaseContext() {
        this.map = new HashMap<>();
    }

    public static DataBaseContext getInstance() {
        if (context == null) {
            synchronized (DataBaseContext.class) {
                if (context == null) {
                    context = new DataBaseContext();
                }
            }
        }
        return context;
    }

    public void push(String catalog,List<TableInfo> tableInfos){
        this.map.put(catalog, tableInfos);
    }

    public List<TableInfo> get(String catalog){
        return this.map.get(catalog);
    }

}
