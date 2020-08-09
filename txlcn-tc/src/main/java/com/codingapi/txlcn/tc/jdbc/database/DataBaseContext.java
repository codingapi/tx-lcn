package com.codingapi.txlcn.tc.jdbc.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分析数据库表的信息
 *
 * 在项目第一次获取到connection 对象的时候开始执行数据库表信息分析，
 * 获取出所有的表字段和主键信息，包括自动增长策略，在SQL幂等性分析的
 * 时候，可以采用这些信息分析业务sql。
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

    public TableList get(String catalog){
        return new TableList(this.map.get(catalog));
    }

    public TableList get(Connection connection) throws SQLException {
        String catalog = connection.getCatalog();
        return new TableList(this.map.get(catalog));
    }

}
