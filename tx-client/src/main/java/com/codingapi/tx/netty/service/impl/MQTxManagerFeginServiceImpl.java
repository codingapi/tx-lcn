package com.codingapi.tx.netty.service.impl;

import com.example.demo.MQTxManagerFegin;
import com.codingapi.tx.netty.service.MQTxManagerFeginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author yizhishang
 */
@Service
public class MQTxManagerFeginServiceImpl implements MQTxManagerFeginService {

    @Autowired
    private ApplicationContext spring;

    @Autowired
    private MQTxManagerFegin mqTxManagerFegin;

    public void reloadMqTxManagerFegin() {
//        if(mqTxManagerFegin == null){
//            this.mqTxManagerFegin = spring.getBean(MQTxManagerFegin.class);
//        }
    }

    /**
     * 检查并清理事务数据
     *
     * @param groupId    事务组id
     * @param waitTaskId 任务id
     * @return 事务状态
     */
    @Override
    public String cleanNotifyTransactionHttp(String groupId, String waitTaskId) {
        reloadMqTxManagerFegin();
        return mqTxManagerFegin.cleanNotifyTransactionHttp(groupId, waitTaskId);
    }

    /**
     * 记录补偿事务数据到tm
     */
    @Override
    public String sendCompensateMsg(long currentTime, String groupId, String model, String address, String uniqueKey, String className, String methodStr, String data, long time,int startError) {
        reloadMqTxManagerFegin();
        return mqTxManagerFegin.sendCompensateMsg(currentTime, groupId, model, address, uniqueKey, className, methodStr, data, time,startError);
    }

    /**
     * 获取TM服务地址
     *
     * @return txServer
     */
    @Override
    public String getServer() {
        reloadMqTxManagerFegin();
        return mqTxManagerFegin.getServer();
    }

}
