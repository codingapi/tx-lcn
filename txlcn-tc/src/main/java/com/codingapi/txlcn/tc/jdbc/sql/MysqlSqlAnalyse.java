package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

    @Override
    public String analyse(String sql,StatementInformation statementInformation)  throws SQLException {
        log.debug("mysql analyse:{}",sql);
        Connection connection =  statementInformation.getConnectionInformation().getConnection();
        List<TableInfo> tableInfoList = DataBaseContext.getInstance().get(connection);
        log.info("tableInfoList:{}",tableInfoList);
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
