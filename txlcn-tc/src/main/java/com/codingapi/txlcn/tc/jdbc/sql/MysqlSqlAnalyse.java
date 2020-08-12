package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import com.codingapi.txlcn.tc.jdbc.database.DataBaseContext;
import com.codingapi.txlcn.tc.jdbc.database.TableInfo;
import com.codingapi.txlcn.tc.jdbc.database.TableList;
import com.codingapi.txlcn.tc.jdbc.sql.strategy.MysqlAnalyseContextEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.insert.Insert;

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
        log.debug("mysql analyse:{}",sql);
        Connection connection =  statementInformation.getConnectionInformation().getConnection();
        TableList tableList = DataBaseContext.getInstance().get(connection);

        Insert insert = (Insert) CCJSqlParserUtil.parse(sql);
        String tableName = insert.getTable().getName();

        TableInfo tableInfo =  tableList.getTable(tableName);
        log.info("tableInfo:{}",tableInfo);

//        if(StringUtils.isEmpty(sql)){
//            throw new TxException("sql is invalid");
//        }

        return MysqlAnalyseContextEnum.valueOf(sql.toUpperCase().substring(0,6)).executeStrategry(sql,statementInformation);
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
