package com.lorne.tx.mq.service;

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
     */
    void closeTransactionGroup(String groupId, int state);


    /**
     * 检查事务状态
     * @param groupId   事务组id
     * @param taskId    任务id
     * @return  事务状态
     */
    int checkTransactionInfo(String groupId, String taskId);



    int httpCheckTransactionInfo(String groupId, String waitTaskId);


    int httpClearTransactionInfo(String groupId, String waitTaskId,boolean isGroup);
}
