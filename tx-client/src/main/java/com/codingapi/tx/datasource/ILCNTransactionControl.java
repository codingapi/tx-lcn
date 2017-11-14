package com.codingapi.tx.datasource;

import com.codingapi.tx.datasource.service.DataSourceService;

/**
 * LCN 代理事务协调控制
 * create by lorne on 2017/9/6
 */
public interface ILCNTransactionControl {

    /**
     * 是否是同一个事务下
     * @param group 事务组id
     * @return  true是，false否
     */
    boolean hasGroup(String group);

    /**
     * 设置数据库连接池
     * @param dataSourceService 数据操作服务
     */
    void setDataSourceService(DataSourceService dataSourceService);

    /**
     * 是否是 事务操作
     * @return true是，false否
     */
    boolean hasTransaction();
}
