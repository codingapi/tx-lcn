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
        //1：主动询问tx-m结果状态。
        int rs = txManagerService.cleanNotifyTransaction(groupId, waitTaskId);
        //执行成功或失败则存储结果唤醒。
        if (rs == 1 || rs == 0) {
            //将tx-m返回的结果存储。
            waitTask.setState(rs);
            //唤醒任务
            waitTask.signalTask();

            return;
        }
        //2：走到这里说明网络或连接错误，通过http请求询问tx-m结果状态，同通知txm清理事务数据。
        rs = txManagerService.cleanNotifyTransactionHttp(groupId, waitTaskId);
        if (rs == 1 || rs == 0) {
            waitTask.setState(rs);
            waitTask.signalTask();

            return;
        }

        //3：走到这里说明tx-m异常，则添加到补偿队列
        waitTask.setState(-100);
        waitTask.signalTask();

    }
}
