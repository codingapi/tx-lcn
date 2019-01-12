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
 * create by lorne on 2018/1/5
 */


@Aspect
@Component
@Slf4j
public class DataSourceAspect implements Ordered {

    @Autowired
    private TxClientConfig txClientConfig;

    @Autowired
    private DTXResourceServiceExecutor DTXResourceServiceExecutor;


    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        return DTXResourceServiceExecutor.around(point);
    }


    @Override
    public int getOrder() {
        return txClientConfig.getResourceOrder();
    }


}
