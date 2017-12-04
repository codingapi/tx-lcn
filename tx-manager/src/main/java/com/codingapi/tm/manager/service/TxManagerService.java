package com.codingapi.tm.manager.service;

import com.codingapi.tm.netty.model.TxGroup;

/**
 * Created by lorne on 2017/6/7.
 */

public interface TxManagerService {


    /**
     * 创建事物组
     *
     * @param groupId 补偿事务组id
     */
    TxGroup createTransactionGroup(String groupId);


    /**
     * 添加事务组子对象
     *
     * @return
     */

    TxGroup addTransactionGroup(String groupId, String taskId,int isGroup, String modelName, String methodStr);


    boolean closeTransactionGroup(String groupId,int state);


    void dealTxGroup(TxGroup txGroup, boolean hasOk );

    void deleteTxGroup(TxGroup txGroup);


    /**
     * 检查事务组数据
     * @param groupId   事务组id
     * @param taskId    任务id
     * @return  本次请求的是否提交 1提交 0回滚
     */
    int cleanNotifyTransaction(String groupId, String taskId);




}
