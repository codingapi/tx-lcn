package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.codingapi.txlcn.tc.jdbc.sql.analyse.SqlDetailAnalyse;
import net.sf.jsqlparser.statement.Statement;

import java.util.List;

/**
 * @author Gz.
 * @description:
 * @date 2020-09-02 22:51:37
 */

public class SqlDetailAnalyseFactory {

    private List<SqlDetailAnalyse> analyseHandlers;

    public SqlDetailAnalyseFactory(List<SqlDetailAnalyse> analyseHandlers) {
        this.analyseHandlers = analyseHandlers;
    }

    public  SqlDetailAnalyse getInvokeStrategy(String sqlType) {
        for(SqlDetailAnalyse handler : analyseHandlers){
            if(handler.preAnalyse(sqlType)){
                return handler;
            }
        }
        return null;
    }

}
