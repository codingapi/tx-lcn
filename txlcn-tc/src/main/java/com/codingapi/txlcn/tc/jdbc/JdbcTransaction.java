package com.codingapi.txlcn.tc.jdbc;

import com.codingapi.txlcn.tc.jdbc.log.TransactionLog;
import lombok.Getter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Jdbc事务消息
 * @author lorne
 * @date 2020/7/1
 * @description
 */
public class JdbcTransaction {

    /**
     * 原始的连接对象(未代理)
     */
    @Getter
    private Connection connection;

    /**
     * sql日志对象.
     */
    @Getter
    private List<TransactionLog> transactionLogs;

    public static JdbcTransaction current(){
        return JdbcTransactionThreadLocal.current();
    }

    protected JdbcTransaction(Connection connection){
        this.connection = connection;
        this.transactionLogs = new ArrayList<>();
        JdbcTransactionThreadLocal.push(this);
    }

    public static void clear() {
        JdbcTransactionThreadLocal.push(null);
    }

    public void add(TransactionLog transactionLog) {
        transactionLogs.add(transactionLog);
    }

    public List<Long> logIds(){
        return transactionLogs.stream().map(TransactionLog::getId).collect(Collectors.toList());
    }

    public void init() {
        JdbcTransactionThreadLocal.push(this);
    }
}
