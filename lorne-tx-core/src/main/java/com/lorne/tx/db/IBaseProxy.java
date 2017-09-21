package com.lorne.tx.db;

import com.lorne.tx.db.service.DataSourceService;

/**
 * create by lorne on 2017/9/6
 */
public interface IBaseProxy {

    boolean hasGroup(String group);

    void setDataSourceService(DataSourceService dataSourceService);
}
