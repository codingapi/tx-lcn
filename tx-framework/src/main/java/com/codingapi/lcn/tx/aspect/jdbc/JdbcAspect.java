package com.codingapi.lcn.tx.aspect.jdbc;

import com.codingapi.lcn.tx.annotation.TxTransaction;
import com.codingapi.lcn.tx.threadlocal.TxTransactionLocal;
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
public class JdbcAspect implements Ordered {


    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Object around(ProceedingJoinPoint point)throws Throwable{

        log.info("getConnection-start---->");

        TxTransactionLocal txTransactionLocal =  TxTransactionLocal.current();
        log.info("txTransactionLocal->{}",txTransactionLocal);
        Object obj  = point.proceed();
        log.info("connection-->"+obj);

        log.info("getConnection-end---->");

        return obj;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
