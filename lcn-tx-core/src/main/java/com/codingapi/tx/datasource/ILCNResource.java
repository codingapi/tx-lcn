package com.codingapi.tx.datasource;

import com.codingapi.tx.framework.task.TxTask;

/**
 *
 * LCN 资源连接对象
 *
 *
 * create by lorne on 2017/8/22
 */
public interface ILCNResource<T> {

    void close() throws Exception;

    TxTask getWaitTask();

    String getGroupId();

    void transaction() throws Exception;

    void setHasIsGroup(boolean isGroup);

    T get();

    int getMaxOutTime();
}
