package com.codingapi.txlcn.tc.jdbc;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
public class JdbcContext {

    private Map<String, Connection> connectionMap;

    private JdbcContext(){
        connectionMap = new ConcurrentHashMap<>();
    }

    private static JdbcContext context;

    public static JdbcContext getInstance() {
        if (context == null) {
            synchronized (JdbcContext.class) {
                if (context == null) {
                    context = new JdbcContext();
                }
            }
        }
        return context;
    }

    public void push(String key,Connection connection){
        connectionMap.put(key, connection);
    }

    public Connection get(String key){
        return connectionMap.get(key);
    }
}
