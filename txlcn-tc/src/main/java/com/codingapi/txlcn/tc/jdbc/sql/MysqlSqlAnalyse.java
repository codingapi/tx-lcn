package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.AnalyseStrategryFactory;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.MysqlAnalyseEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author lorne
 * @date 2020/7/3
 * @description
 */
@Slf4j
public class MysqlSqlAnalyse implements SqlAnalyse {

    @Override
    public String sqlType() {
        return "mysql";
    }

    @SneakyThrows
    @Override
    public String analyse(String sql,StatementInformation statementInformation)  throws SQLException {
        log.debug("mysql analyse:{}", sql);
        Connection connection = statementInformation.getConnectionInformation().getConnection();
        // sql.toUpperCase().substring(0,6) 这样实现有风险
        // if else 实现并不是很优雅
        CCJSqlParserManager parser = new CCJSqlParserManager();
        Statement stmt = parser.parse(new StringReader(sql));
        if (stmt instanceof Insert) {
            return AnalyseStrategryFactory.getInvokeStrategy(MysqlAnalyseEnum.INSERT.name()).mysqlAnalyseStrategy(sql,connection,stmt);
        } else if (stmt instanceof Update) {
            return AnalyseStrategryFactory.getInvokeStrategy(MysqlAnalyseEnum.UPDATE.name()).mysqlAnalyseStrategy(sql,connection,stmt);
        } else if (stmt instanceof Delete) {
            return AnalyseStrategryFactory.getInvokeStrategy(MysqlAnalyseEnum.DELETE.name()).mysqlAnalyseStrategy(sql,connection,stmt);
        }
        return sql;
    }
    @Override
    public boolean preAnalyse(String sql) {
        // SQL类型检查，只有对CUD(CURD)操作做处理
        String newSql = sql.trim().toUpperCase();
        if(newSql.startsWith("INSERT")){
            return true;
        }
        if(newSql.startsWith("UPDATE")){
            return true;
        }
        if(newSql.startsWith("DELETE")) {
            return true;
        }
        return false;
    }
}
