package com.codingapi.tm.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tm.manager.service.TxManagerService;
import com.codingapi.tm.netty.service.IActionService;
import com.lorne.core.framework.utils.task.ConditionUtils;
import com.lorne.core.framework.utils.task.IBack;
import com.lorne.core.framework.utils.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通知事务单元
 * create by lorne on 2017/11/11
 */
@Service(value = "t")
public class ActionTServiceImpl implements IActionService{


    @Autowired
    private TxManagerService txManagerService;

    @Override
    public String execute(String modelName,String key,JSONObject params) {
        String res = "";
        final String data = params.getString("d");
        Task task = ConditionUtils.getInstance().getTask(key);
        if (task != null) {
            task.setBack(new IBack() {
                @Override
                public Object doing(Object... objs) throws Throwable {
                    return data;
                }
            });
            task.signalTask();
        }
        return res;
    }
}
