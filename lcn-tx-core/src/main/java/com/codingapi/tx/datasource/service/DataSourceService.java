package com.codingapi.tx.datasource.service;

import com.lorne.core.framework.utils.task.Task;


/**
 * create by lorne on 2017/7/29
 */
public interface DataSourceService {


    void schedule(String groupId, Task waitTask);


}
