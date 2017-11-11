package com.codingapi.tx.control.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.control.service.TransactionControlService;
import com.codingapi.tx.framework.task.TaskGroup;
import com.codingapi.tx.framework.task.TaskGroupManager;
import com.codingapi.tx.framework.utils.SocketUtils;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * create by lorne on 2017/11/11
 */
@Service
public class TransactionControlServiceImpl implements TransactionControlService{

    private Logger logger = LoggerFactory.getLogger(TransactionControlServiceImpl.class);


    private String notifyWaitTask(TaskGroup task, int state) {
        String res;
        task.setState(state);
        task.signalTask();
        int count = 0;

        while (true) {
            if (task.isRemove()) {

                if(task.getState()==0||task.getState()==1){
                    res = "1";
                }else{
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

    @Override
    public void notifyTransactionMsg(ChannelHandlerContext ctx,JSONObject resObj, String json) {

        String action = resObj.getString("a");
        String key = resObj.getString("k");
        String res = "0";

        switch (action) {

            case "t": {
                //通知提醒
                final int state = resObj.getInteger("c");
                String taskId = resObj.getString("t");
                TaskGroup task = TaskGroupManager.getInstance().getTaskGroup(taskId);
                logger.info("接受通知数据->" + json);
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
                }else{
                    res = "0";
                }
                break;
            }
        }

        JSONObject data = new JSONObject();
        data.put("k", key);
        data.put("a", action);

        JSONObject params = new JSONObject();
        params.put("d", res);
        data.put("p", params);

        SocketUtils.sendMsg(ctx, data.toString());
        logger.info("返回通知状态->" + data.toString());
    }
}
