package com.codingapi.tx.datasource.aspect;

import com.codingapi.tx.datasource.ILCNConnection;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;

/**
 * create by lorne on 2018/1/5
 */


@Aspect
@Component
public class DataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

    @Autowired
    private ILCNConnection lcnConnection;


    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint point)throws Throwable{

        logger.debug("getConnection-start---->");

        Connection connection = lcnConnection.getConnection(point);
        logger.debug("connection-->"+connection);

        logger.debug("getConnection-end---->");

        return connection;
    }

}
