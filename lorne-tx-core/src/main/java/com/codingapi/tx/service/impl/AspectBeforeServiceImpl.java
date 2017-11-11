package com.codingapi.tx.service.impl;

import com.codingapi.tx.annotation.TxTransaction;
import com.codingapi.tx.bean.TxTransactionInfo;
import com.codingapi.tx.bean.TxTransactionLocal;
import com.codingapi.tx.service.AspectBeforeService;
import com.codingapi.tx.service.TransactionServer;
import com.codingapi.tx.service.TransactionServerFactoryService;
import com.codingapi.tx.service.model.TransactionInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * Created by lorne on 2017/7/1.
 */
@Service
public class AspectBeforeServiceImpl implements AspectBeforeService {

    @Autowired
    private TransactionServerFactoryService transactionServerFactoryService;


    public Object around(String groupId,int maxTimeOut, ProceedingJoinPoint point) throws Throwable {

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = point.getTarget().getClass();
        Object[] args = point.getArgs();
        Method thisMethod = clazz.getMethod(method.getName(), method.getParameterTypes());

        TxTransaction transaction = thisMethod.getAnnotation(TxTransaction.class);

        Transactional transactional = thisMethod.getAnnotation(Transactional.class);

        TxTransactionLocal txTransactionLocal = TxTransactionLocal.current();

        TransactionInvocation invocation = new TransactionInvocation(clazz, thisMethod.getName(), args, method.getParameterTypes());

        TxTransactionInfo info = new TxTransactionInfo(transaction,transactional,txTransactionLocal,invocation,groupId,maxTimeOut);

        TransactionServer server = transactionServerFactoryService.createTransactionServer(info);

        return server.execute(point, info);
    }
}
