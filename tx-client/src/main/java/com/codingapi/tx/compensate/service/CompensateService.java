package com.codingapi.tx.compensate.service;


import com.codingapi.tx.compensate.model.CompensateInfo;
import com.codingapi.tx.model.TransactionInvocation;

/**
 * create by lorne on 2017/11/11
 */
public interface CompensateService {


    void saveLocal(CompensateInfo compensateInfo);

    boolean invoke(TransactionInvocation invocation, String groupId, int startState);

}
