package com.codingapi.txlcn.client.core.tcc.resource;

import com.codingapi.txlcn.client.support.resouce.TransactionResourceExecutor;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.function.Supplier;

/**
 * @author 侯存路
 * @date 2018/12/3
 * @company codingApi
 * @description
 */
@Service(value = "transaction_tcc")
public class TCCTransactionResourceExecutor implements TransactionResourceExecutor {

    @Override
    public Connection proxyConnection(Supplier<Connection> connectionSupplier) {
        return connectionSupplier.get();
    }
}
