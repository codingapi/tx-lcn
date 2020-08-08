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
        log.debug("mysql analyse:{}",sql);
        //todo 数据SQL分析
        // 幂等性机制
        // 幂等性机制主要是确保在执行补偿的时候重新提交的数据保持与事务执行时的数据一致，因此为了确保数据的一致性，目前的方案是记录实际影响的数据操作，然后在补偿的时候直接提交这些实际数据。
        // insert 语句分析
        // insert 获取落库后的数据信息
        // 思路
        // 若sql中有主键值，则直接记录下id，然后再查询影响后的数据
        // 若sql中没有主键，则通过主键策略来查询ID，然后再查询影响后的实际数据。
        // update 语句分析
        // update 获取落库后的数据信息
        // 思路
        // 执行完成修改操作以后，按照相同条件下执行查询获取到影响后的实际数据。
        // delete 语句分析
        // delete 获取要真实删除的数据信息
        // 思路
        // 再执行delete之前先查询实际删除数据的id，然后再执行删除操作
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
