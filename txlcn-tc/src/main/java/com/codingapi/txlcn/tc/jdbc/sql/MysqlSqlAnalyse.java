package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.p6spy.common.StatementInformation;
import lombok.extern.slf4j.Slf4j;

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
    public String analyse(String sql,StatementInformation statementInformation) {
        //todo 数据SQL分析
        //1. 幂等性分析
        //1.1. 满足幂等性的直接返回数据.
        //1.2. 不满足幂等性的，调整为幂等性的操作。
        log.debug("mysql analyse:{}",sql);
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
