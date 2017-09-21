package com.lorne.tx.db;

import com.lorne.tx.compensate.model.TransactionRecover;
import com.lorne.tx.db.task.TxTask;

import java.util.List;

/**
 * create by lorne on 2017/8/22
 */
public interface IResource<T> {

    void close() throws Exception;

    TxTask getWaitTask();

    String getGroupId();

    void transaction() throws Exception;

    void setHasIsGroup(boolean isGroup);

    void addCompensate(TransactionRecover recover);

    T get();

    List<TransactionRecover> getCompensateList();

    int getMaxOutTime();
}
