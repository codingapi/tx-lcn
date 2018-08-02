package com.codingapi.tx.springcloud.interceptor;

import com.codingapi.tx.Constants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * LCN 事务拦截器
 * create by lorne on 2018/1/5
 */

@Aspect
@Component
public class TransactionAspect implements Ordered {

    private Logger logger = LoggerFactory.getLogger(TransactionAspect.class);

    @Autowired
    private TxManagerInterceptor txManagerInterceptor;


    @Around("@annotation(com.codingapi.tx.annotation.TxTransaction)")
    public Object transactionRunning(ProceedingJoinPoint point)throws Throwable{
        logger.debug("annotation-TransactionRunning-start---->");
        Object obj =  txManagerInterceptor.around(point);
        logger.debug("annotation-TransactionRunning-end---->");
        return obj;
    }

    @Around("this(com.codingapi.tx.annotation.ITxTransaction) && execution( * *(..))")
    public Object around(ProceedingJoinPoint point)throws Throwable{
        logger.debug("interface-ITransactionRunning-start---->");
        Object obj =  txManagerInterceptor.around(point);
        logger.debug("interface-ITransactionRunning-end---->");
        return obj;
    }


    @Override
    public int getOrder() {
        return Constants.ASPECT_ORDER;
    }


}
