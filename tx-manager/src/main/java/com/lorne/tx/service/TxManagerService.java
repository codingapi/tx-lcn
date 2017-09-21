package com.lorne.tx.service;

import com.lorne.tx.mq.model.TxGroup;

/**
 * Created by lorne on 2017/6/7.
 */

public interface TxManagerService {


    /**
     * 创建事物组
     */
    TxGroup createTransactionGroup();


    /**
     * 添加事务组子对象
     *
     * @return
     */
    TxGroup addTransactionGroup(String groupId,String uniqueKey, String taskId, int isGroup,String modelName);


    /**
     *
     * @param groupId
     * @param taskId
     * @return  1 存在 0不存在 -1 未结束
     */
    int checkTransactionGroup(String groupId,String taskId);


    boolean closeTransactionGroup(String groupId,int state);


    void dealTxGroup(TxGroup txGroup, boolean hasOk );

    void deleteTxGroup(TxGroup txGroup);

    int getDelayTime();


    void clearNotifyData(int time);

    boolean checkClearGroup(String groupId, String taskId, int isGroup);
}
