package com.codingapi.tx.aop.service.impl;

import com.codingapi.tx.aop.bean.TxTransactionInfo;
import com.codingapi.tx.aop.service.TransactionServer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

/**
 * Created by lorne on 2017/6/8.
 */
@Service(value = "txDefaultTransactionServer")
public class TxDefaultTransactionServerImpl implements TransactionServer {



    @Override
    public Object execute(ProceedingJoinPoint point, TxTransactionInfo info) throws Throwable {
        return point.proceed();
    }
}
