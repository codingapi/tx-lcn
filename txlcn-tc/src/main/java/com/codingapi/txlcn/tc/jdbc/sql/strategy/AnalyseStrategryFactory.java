package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import net.sf.jsqlparser.statement.Statement;

import java.util.List;

/**
 * @author Gz.
 * @description:
 * @date 2020-09-02 22:51:37
 */

public class AnalyseStrategryFactory {

    private List<SqlSqlAnalyseHandler> analyseHandlers;

    public AnalyseStrategryFactory(List<SqlSqlAnalyseHandler> analyseHandlers) {
        this.analyseHandlers = analyseHandlers;
    }

    public  SqlSqlAnalyseHandler getInvokeStrategy(String sqlType,Statement statement) {
        for(SqlSqlAnalyseHandler handler : analyseHandlers){
            if(handler.preAnalyse(sqlType,statement)){
                return handler;
            }
        }
        return null;
    }

}
