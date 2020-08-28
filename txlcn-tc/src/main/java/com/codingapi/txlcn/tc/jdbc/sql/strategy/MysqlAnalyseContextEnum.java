package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import net.sf.jsqlparser.JSQLParserException;

import java.sql.Connection;
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
        public String executeStrategry(String sql, Connection connection) throws SQLException, JSQLParserException {
            return strategry.mysqlAnalyseStrategry(sql,connection);
        }
    },
    INSERT(){
        private MysqlSqlAnalyseStrategry strategry = new MysqlInsertAnalyseStrategry();
        @Override
        public String executeStrategry(String sql,  Connection connection) throws SQLException, JSQLParserException {
            return strategry.mysqlAnalyseStrategry(sql,connection);
        }

    },
    UPDATE(){
        private MysqlSqlAnalyseStrategry strategry = new MysqlUpdateAnalyseStrategry();
        @Override
        public String executeStrategry(String sql,  Connection connection) throws SQLException, JSQLParserException {
            return strategry.mysqlAnalyseStrategry(sql,connection);
        }

    };


    public abstract String executeStrategry(String sql,  Connection connection) throws SQLException, JSQLParserException;
}
