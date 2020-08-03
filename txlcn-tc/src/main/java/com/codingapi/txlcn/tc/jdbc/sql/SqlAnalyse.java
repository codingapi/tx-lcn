package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.p6spy.common.StatementInformation;

/**
 * @author lorne
 * @date 2020/7/3
 * SQL幂等性分析接口，适配各种数据库做差异性适配
 */
public interface SqlAnalyse {

    /**
     * 数据库类型
     * @return
     */
    String sqlType();

    /**
     * SQL幂等性分析
     * @param sql SQL数据
     * @param statementInformation 执行返回数据
     * @return 满足幂等性的SQL
     */
    String analyse(String sql,StatementInformation statementInformation);

    /**
     * SQL 分析判断检查
     * 仅当CUD操作才需要做幂等性检查
     * @param sql 执行的SQL
     * @return
     */
    boolean preAnalyse(String sql);

}
