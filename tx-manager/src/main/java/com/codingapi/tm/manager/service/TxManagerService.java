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

    TxGroup addTransactionGroup(String groupId, String taskId,int isGroup, String channelAddress, String methodStr);


    /**
     * 关闭事务组
     * @param groupId 事务组id
     * @param state    事务状态
     * @return  0 事务存在补偿 1 事务正常  -1 事务强制回滚
     */
    int closeTransactionGroup(String groupId,int state);


    void dealTxGroup(TxGroup txGroup, boolean hasOk );


    /**
     * 删除事务组
     * @param txGroup 事务组
     */
    void deleteTxGroup(TxGroup txGroup);


    /**
     * 获取事务组信息
     * @param groupId    事务组id
     * @return  事务组
     */
    TxGroup getTxGroup(String groupId);


    /**
     * 获取事务组的key
     * @param groupId 事务组id
     * @return key
     */
    String getTxGroupKey(String groupId);


    /**
     * 检查事务组数据
     * @param groupId   事务组id
     * @param taskId    任务id
     * @return  本次请求的是否提交 1提交 0回滚
     */
    int cleanNotifyTransaction(String groupId, String taskId);


    /**
     * 设置强制回滚事务
     * @param groupId 事务组id
     * @return  true 成功 false 失败
     */
    boolean rollbackTransactionGroup(String groupId);
}
