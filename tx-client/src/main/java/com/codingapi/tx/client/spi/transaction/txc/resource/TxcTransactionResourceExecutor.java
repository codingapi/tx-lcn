package com.codingapi.tx.client.spi.transaction.txc.resource;

import com.codingapi.tx.client.support.resouce.TransactionResourceExecutor;
import com.codingapi.tx.jdbcproxy.p6spy.spring.ConnectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.function.Supplier;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component("transaction_txc")
public class TxcTransactionResourceExecutor implements TransactionResourceExecutor {

    private final ConnectionHelper connectionHelper;

    @Autowired
    public TxcTransactionResourceExecutor(ConnectionHelper connectionHelper) {
        this.connectionHelper = connectionHelper;
    }


    @Override
    public Connection proxyConnection(Supplier<Connection> connectionSupplier) {
        return connectionHelper.proxy(connectionSupplier.get());
    }
}
