package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import net.sf.jsqlparser.JSQLParserException;

import java.sql.SQLException;

/**
 * @author Gz.
 * @description: 策略模式聚合类
 * @date 2020-08-13 23:08:26
 */
public  enum  MysqlAnalyseContextEnum {

    DELETE(){
        private MysqlSqlAnalyseStrategry strategry = new MysqlDeleteAnalyseStrategry();
        @Override
        public String executeStrategry(String sql, StatementInformation statementInformation) throws SQLException, JSQLParserException {
            return strategry.MysqlAnalyseStrategry(sql,statementInformation);
        }
    },
    INSERT(){
        private MysqlSqlAnalyseStrategry strategry = new MysqlInsertAnalyseStrategry();
        @Override
        public String executeStrategry(String sql, StatementInformation statementInformation) throws SQLException, JSQLParserException {
            return strategry.MysqlAnalyseStrategry(sql,statementInformation);
        }

    },
    UPDATE(){
        private MysqlSqlAnalyseStrategry strategry = new MysqlUpdateAnalyseStrategry();
        @Override
        public String executeStrategry(String sql, StatementInformation statementInformation) throws SQLException, JSQLParserException {
            return strategry.MysqlAnalyseStrategry(sql,statementInformation);
        }

    };


    public abstract String executeStrategry(String sql, StatementInformation statementInformation) throws SQLException, JSQLParserException;
}
