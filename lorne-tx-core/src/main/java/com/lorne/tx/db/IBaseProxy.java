package com.lorne.tx.db;

import com.lorne.tx.db.service.DataSourceService;

/**
 * create by lorne on 2017/9/6
 */
public interface IBaseProxy {

    /**
     * 是否是同一个事务下
     * @param group 事务组id
     * @return  true是，false否
     */
    boolean hasGroup(String group);

    /**
     * 设置数据库连接池
     * @param dataSourceService
     */
    void setDataSourceService(DataSourceService dataSourceService);

    /**
     * 是否是 事务操作
     * @return true是，false否
     */
    boolean hasTransaction();
}
