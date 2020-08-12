package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import net.sf.jsqlparser.JSQLParserException;

import java.sql.SQLException;

public  enum  MysqlAnalyseContextEnum {

    DELETE(){
        private MysqlSqlAnalyseStrategry strategry = new MysqlDeleteAnalyseStrategry();
        @Override
        public String executeStrategry(String sql, StatementInformation statementInformation) throws SQLException, JSQLParserException {
            return strategry.MysqlAnalyseStrategry(sql,statementInformation);
        }
    };


    public abstract String executeStrategry(String sql, StatementInformation statementInformation) throws SQLException, JSQLParserException;
}
