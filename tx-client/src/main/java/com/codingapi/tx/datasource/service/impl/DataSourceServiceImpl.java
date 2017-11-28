package com.codingapi.tx.datasource.service.impl;

import com.codingapi.tx.datasource.service.DataSourceService;
import com.codingapi.tx.netty.service.MQTxManagerService;
import com.lorne.core.framework.utils.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/7/29
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {


    @Autowired
    private MQTxManagerService txManagerService;


    @Override
    public void schedule(String groupId, Task waitTask) {


        String waitTaskId = waitTask.getKey();
        int rs = txManagerService.cleanNotifyTransaction(groupId, waitTaskId);
        if (rs == 1 || rs == 0) {
            waitTask.setState(rs);
            waitTask.signalTask();

            return;
        }
        rs = txManagerService.cleanNotifyTransactionHttp(groupId, waitTaskId);
        if (rs == 1 || rs == 0) {
            waitTask.setState(rs);
            waitTask.signalTask();

            return;
        }

        //添加到补偿队列
        waitTask.setState(-100);
        waitTask.signalTask();

    }
}
