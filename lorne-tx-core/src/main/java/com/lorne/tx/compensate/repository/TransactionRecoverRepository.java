package com.lorne.tx.compensate.repository;

import com.lorne.tx.compensate.model.TransactionRecover;

import java.util.List;

/**
 * <p>Description: .</p>
 * <p>Copyright: 2015-2017 happylifeplat.com All Rights Reserved</p>
 * 定义事务恢复资源接口
 *
 * @author yu.xiao@happylifeplat.com
 * @version 1.0
 * @since JDK 1.8
 */

public interface TransactionRecoverRepository {

    /**
     * 创建本地事务对象
     *
     * @param transactionRecover 事务对象
     * @return rows
     */
    int create(TransactionRecover transactionRecover);

    /**
     * 删除对象
     *
     * @param id 事务对象id
     * @return rows
     */
    int remove(String id);


    /**
     * 更改事务对象
     *
     * @param id           事务对象id
     * @param retriedCount 执行次数
     * @param state        数据库状态
     * @return rows
     */
    int update(String id,int state, int retriedCount);


    /**
     * 获取需要提交的事务
     *
     * @return List<TransactionRecover>
     */
    List<TransactionRecover> findAll(int state);

    /**
     * 获取需要补偿的事务
     *
     * @return List<TransactionRecover>
     */
    List<TransactionRecover> loadCompensateList(int time);

    /**
     * 创建表等操作
     */
    void init(String tableName,String unique);


    long countCompensateByTaskId(String taskId);

    TransactionRecover getCompensateByTaskId(String taskId);

    List<TransactionRecover> getCompensateByGroupId(String groupId);
}
