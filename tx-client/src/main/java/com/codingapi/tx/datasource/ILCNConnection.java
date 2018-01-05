package com.codingapi.tx.datasource;

import org.aspectj.lang.ProceedingJoinPoint;

import java.sql.Connection;

/**
 * create by lorne on 2018/1/5
 */
public interface ILCNConnection {

    Connection getConnection(ProceedingJoinPoint point) throws Throwable;


}
