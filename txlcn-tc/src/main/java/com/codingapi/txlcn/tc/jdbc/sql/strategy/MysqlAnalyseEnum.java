package com.codingapi.txlcn.tc.jdbc.sql.strategy;

import net.sf.jsqlparser.JSQLParserException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Gz.
 * @description: 策略模式聚合类
 * @date 2020-08-13 23:08:26
 */
public  enum  MysqlAnalyseEnum {

    DELETE(),
    INSERT(),
    UPDATE(),;
}
