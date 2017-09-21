package com.lorne.tx.service.impl;

import com.lorne.tx.annotation.TxTransaction;
import com.lorne.tx.bean.TxTransactionCompensate;
import com.lorne.tx.bean.TxTransactionInfo;
import com.lorne.tx.bean.TxTransactionLocal;
import com.lorne.tx.compensate.model.TransactionInvocation;
import com.lorne.tx.service.AspectBeforeService;
import com.lorne.tx.service.TransactionServer;
import com.lorne.tx.service.TransactionServerFactoryService;
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

        TxTransactionCompensate compensate = TxTransactionCompensate.current();

        TransactionInvocation invocation = new TransactionInvocation(clazz, thisMethod.getName(), args, method.getParameterTypes());

        TxTransactionInfo info = new TxTransactionInfo(transaction,transactional,txTransactionLocal,groupId,maxTimeOut,compensate,invocation);

        TransactionServer server = transactionServerFactoryService.createTransactionServer(info);

        return server.execute(point, info);
    }
}
