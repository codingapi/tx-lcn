package com.codingapi.tx.datasource.relational;

import com.codingapi.tx.datasource.ILCNResource;

import java.sql.Connection;

/**
 * create by lorne on 2017/12/7
 */
public interface LCNConnection extends  Connection,ILCNResource<Connection> {
}
