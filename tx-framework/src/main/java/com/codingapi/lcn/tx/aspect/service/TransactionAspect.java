package com.codingapi.lcn.tx.aspect.service;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author lorne
 * @date 2018/8/30
 * @description
 */
@Component
@Aspect
@Slf4j
public class TransactionAspect implements Ordered {

    @Around("@annotation(com.codingapi.lcn.tx.annotation.TxTransaction)")
    public Object transactionRunning(ProceedingJoinPoint point)throws Throwable{
        log.info("annotation-TransactionRunning-start---->");
        Object obj =  point.proceed();
        log.info("annotation-TransactionRunning-end---->");
        return obj;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
