package com.codingapi.tx.service;

import com.codingapi.tx.service.model.TxServer;
import com.codingapi.tx.service.model.TxState;

/**
 * Created by lorne on 2017/7/1.
 */
public interface TxService {

    TxServer getServer();

    TxState getState();

    String sendMsg(String model,String msg);

    /**
     * 检查并清理事务数据
     * @param groupId   事务组Id
     * @param taskId    任务Id
     * @param isGroup   是否合并事务
     * @return  事务状态
     */
    boolean clearTransaction(String groupId, String taskId, int isGroup);

    /**
     * 检查事务数据
     * @param groupId   事务组Id
     * @param taskId    任务Id
     * @return  事务状态
     */
    int getTransaction(String groupId, String taskId);




    /**
     * 保存事务补偿日志信息
     * @param groupId    事务组id
     * @param model     模块名称
     * @param uniqueKey 唯一标示
     * @param className 事务启动类
     * @param method    事务启动方法
     * @param data      切面数据
     * @param time      执行时间
     * @return  是否保存成功
     */
    boolean sendCompensateMsg(String groupId, String model, String uniqueKey, String className, String method, String data, int time);
}
