package com.lorne.tx.compensate.service;


import com.lorne.tx.compensate.model.TransactionRecover;
import com.lorne.tx.compensate.repository.TransactionRecoverRepository;

/**
 * 补偿操作实现方法
 * Created by lorne on 2017/7/12.
 */
public interface BlockingQueueService {

    void setTransactionRecover(TransactionRecoverRepository recoverRepository);

   // String save(TransactionInvocation transactionInvocation, String groupId, String taskId);

    boolean delete(String id);

    void init(String tableName,String unique);


    void save(TransactionRecover recover);

}
