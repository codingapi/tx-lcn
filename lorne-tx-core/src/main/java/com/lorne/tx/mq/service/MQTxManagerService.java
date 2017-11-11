package com.lorne.tx.mq.service;

import com.lorne.tx.bean.TxTransactionInfo;
import com.lorne.tx.mq.model.TxGroup;


/**
 * Created by lorne on 2017/6/7.
 */
public interface MQTxManagerService {


    /**
     * 创建事务组
     *
     * @return  事务组TxGroup
     */
    TxGroup createTransactionGroup();


    /**
     * 添加事务组子对象
     * @param groupId   事务组id
     * @param taskId    任务Id
     * @param isGroup   是否合并到事务组 true合并 false不合并
     * @return  事务组TxGroup
     */
    TxGroup addTransactionGroup(String groupId, String taskId, boolean isGroup);


    /**
     * 关闭事务组-进入事务提交第一阶段
     *
     * @param groupId   事务组id
     * @param state     提交或者回滚 1提交0回滚
     * @return 1 成功 0失败
     */
    int closeTransactionGroup(String groupId, int state);


    /**
     * 检查事务状态 通过netty管道
     * @param groupId   事务组id
     * @param taskId    任务id
     * @return  事务状态
     */
    int checkTransactionInfo(String groupId, String taskId);


    /**
     * 检查事务状态   通过http请求
     * @param groupId 事务组id
     * @param waitTaskId 任务id
     * @return 事务状态
     */
    int getTransaction(String groupId, String waitTaskId);


    /**
     * 检查并清理事务数据
     * @param groupId   事务组id
     * @param waitTaskId    任务id
     * @param isGroup   是否合并事务
     * @return  事务状态
     */
    int clearTransaction(String groupId, String waitTaskId, boolean isGroup);

    /**
     * 记录补偿事务数据到tm
     * @param groupId   事务组Id
     * @param time  执行时间
     * @param info  事务信息
     */
    void sendCompensateMsg(String groupId, long time, TxTransactionInfo info);


    /**
     * 获取TM服务地址
     * @return txServer
     */
    String httpGetServer();

}
