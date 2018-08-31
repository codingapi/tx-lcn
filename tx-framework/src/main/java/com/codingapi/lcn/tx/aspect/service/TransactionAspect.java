package com.codingapi.lcn.tx.aspect.service;

import com.codingapi.lcn.tx.annotation.TxTransaction;
import com.codingapi.lcn.tx.bean.TransactionInvocation;
import com.codingapi.lcn.tx.config.TxTransactionConfig;
import com.codingapi.lcn.tx.threadlocal.TxTransactionLocal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author lorne
 * @date 2018/8/30
 * @description
 */
@Component
@Aspect
@Slf4j
public class TransactionAspect implements Ordered {

    @Autowired
    private TxTransactionConfig txTransactionConfig;


    @Around("@annotation(com.codingapi.lcn.tx.annotation.TxTransaction)")
    public Object transactionRunning(ProceedingJoinPoint point)throws Throwable{
        log.info("annotation-TransactionRunning-start---->");

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = point.getTarget().getClass();
        Object[] args = point.getArgs();
        Method thisMethod = clazz.getMethod(method.getName(), method.getParameterTypes());
        TransactionInvocation invocation = new TransactionInvocation(clazz, thisMethod.getName(),args, method.getParameterTypes(),thisMethod.toString());
        TxTransaction transaction = thisMethod.getAnnotation(TxTransaction.class);
        TxTransactionLocal transactionLocal = TxTransactionLocal.current();

        if(transactionLocal==null){
            transactionLocal = new TxTransactionLocal();
        }
        transactionLocal.setInvocation(invocation);
        transactionLocal.setTransaction(transaction);
        TxTransactionLocal.setCurrent(transactionLocal);

        Object obj =  point.proceed();
        log.info("annotation-TransactionRunning-end---->");
        return obj;
    }


    @Override
    public int getOrder() {
        return txTransactionConfig.getTransactionAspectOrder();
    }
}
