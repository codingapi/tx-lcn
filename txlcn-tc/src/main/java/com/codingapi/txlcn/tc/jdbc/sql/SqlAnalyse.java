package com.codingapi.txlcn.tc.jdbc.sql;

import com.codingapi.txlcn.p6spy.common.StatementInformation;

import java.sql.SQLException;

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
     * 幂等性机制主要是确保在执行补偿的时候重新提交的数据保持与事务执行时的数据一致，因此为了确保数据的一致性，目前的方案是记录实际影响的数据操作，然后在补偿的时候直接提交这些实际数据。
     * insert 语句分析
     * insert 获取落库后的数据信息
     * 实现思路
     * 若sql中有主键值，则直接记录下id，然后再查询影响后的数据
     * 若sql中没有主键，则通过主键策略来查询ID，然后再查询影响后的实际数据。
     * update 语句分析
     * update 获取落库后的数据信息
     * 实现思路
     * 执行完成修改操作以后，按照相同条件下执行查询获取到影响后的实际数据。
     * delete 语句分析
     * delete 获取要真实删除的数据信息
     * 实现思路
     * 再执行delete之前先查询实际删除数据的id，然后再执行删除操作
     * @param sql SQL数据
     * @param statementInformation 执行返回数据
     * @return 满足幂等性的SQL
     */
    String analyse(String sql,StatementInformation statementInformation)  throws SQLException;

    /**
     * SQL 分析判断检查
     * 仅当CUD操作才需要做幂等性检查
     * @param sql 执行的SQL
     * @return
     */
    boolean preAnalyse(String sql);

}
