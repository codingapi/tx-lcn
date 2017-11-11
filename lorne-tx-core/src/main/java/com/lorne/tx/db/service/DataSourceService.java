package com.lorne.tx.db.service;

import com.lorne.core.framework.utils.task.Task;
//import com.lorne.tx.compensate.model.TransactionRecover;

import java.util.List;

/**
 * create by lorne on 2017/7/29
 */
public interface DataSourceService {


    void schedule(String groupId, Task waitTask);

   // void deleteCompensates(List<TransactionRecover> compensates);

//    void deleteCompensateId(String compensateId);
//
//    void saveTransactionRecover(TransactionRecover recover);
}
