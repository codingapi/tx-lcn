package com.codingapi.tx.service;

import com.codingapi.tx.bean.TxTransactionInfo;
import org.aspectj.lang.ProceedingJoinPoint;


/**
 * Created by lorne on 2017/6/8.
 */
public interface TransactionServer {


    // void execute();

    Object execute(ProceedingJoinPoint point, TxTransactionInfo info) throws Throwable;

}
