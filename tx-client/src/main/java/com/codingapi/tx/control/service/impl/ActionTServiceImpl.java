package com.codingapi.tx.control.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.control.service.IActionService;
import com.codingapi.tx.datasource.ILCNTransactionControl;
import com.codingapi.tx.framework.task.TaskGroup;
import com.codingapi.tx.framework.task.TaskGroupManager;
import com.codingapi.tx.framework.task.TaskState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通知提交
 * create by lorne on 2017/11/13
 */
@Service(value = "t")
public class ActionTServiceImpl implements IActionService {


    private Logger logger = LoggerFactory.getLogger(ActionTServiceImpl.class);

    @Autowired
    private ILCNTransactionControl transactionControl;

    @Override
    public String execute(JSONObject resObj, String json) {
        String res;
        //通知提醒
        final int state = resObj.getInteger("c");
        String taskId = resObj.getString("t");
        if(transactionControl.executeTransactionOperation()) {
            TaskGroup task = TaskGroupManager.getInstance().getTaskGroup(taskId);
            logger.info("accept notify data ->" + json);
            if (task != null) {
                if (task.isAwait()) {   //已经等待
                    res = notifyWaitTask(task, state);
                } else {
                    int index = 0;
                    while (true) {
                        if (index > 500) {
                            res = "0";
                            break;
                        }
                        if (task.isAwait()) {   //已经等待
                            res = notifyWaitTask(task, state);
                            break;
                        }
                        index++;
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                res = "0";
            }
        }else{
            //非事务操作
            res = "1";
            transactionControl.autoNoTransactionOperation();
        }
        logger.info("accept notify response res ->" + res);
        return res;
    }

    private String notifyWaitTask(TaskGroup task, int state) {
        String res;
        task.setState(state);
        task.signalTask();
        int count = 0;

        while (true) {
            if (task.isRemove()) {

                if (task.getState() == TaskState.rollback.getCode()
                    || task.getState() == TaskState.commit.getCode()) {

                    res = "1";
                } else {
                    res = "0";
                }
                break;
            }
            if (count > 1000) {
                //已经通知了，有可能失败.
                res = "2";
                break;
            }

            count++;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return res;
    }
}
