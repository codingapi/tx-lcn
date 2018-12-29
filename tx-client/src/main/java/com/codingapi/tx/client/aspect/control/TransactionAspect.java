package com.codingapi.tx.client.aspect.control;

import com.codingapi.tx.client.config.TxClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * LCN 事务拦截器
 * create by lorne on 2018/1/5
 */

@Aspect
@Component
@Slf4j
public class TransactionAspect implements Ordered {

    @Autowired
    private TxClientConfig txClientConfig;

    @Autowired
    private AspectBeforeServiceExecutor aspectBeforeServiceExecutor;


    @Around("@annotation(com.codingapi.tx.commons.annotation.TxTransaction)")
    public Object transactionRunning(ProceedingJoinPoint point)throws Throwable{
        log.info("TX-LCN local start---->");
        Object obj =  aspectBeforeServiceExecutor.around(point);
        log.info("TX-LCN local end------>");
        return obj;
    }

    @Around("this(com.codingapi.tx.commons.annotation.ITxTransaction) && execution( * *(..))")
    public Object around(ProceedingJoinPoint point)throws Throwable{
        log.info("interface-ITransactionRunning-start---->");
        Object obj =  aspectBeforeServiceExecutor.around(point);
        log.info("interface-ITransactionRunning-end---->");
        return obj;
    }


    @Override
    public int getOrder() {
        return txClientConfig.getControlOrder();
    }


}
