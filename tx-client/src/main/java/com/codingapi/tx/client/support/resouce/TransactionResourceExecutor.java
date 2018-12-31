package com.codingapi.tx.client.support.resouce;

import java.sql.Connection;
import java.util.function.Supplier;

/**
 * @author lorne
 * @date 2018/12/2
 * @description
 */
public interface TransactionResourceExecutor {

    /**
     * 获取资源连接
     *
     * @param connectionSupplier Connection提供者
     * @return
     * @throws Throwable
     */
    Connection proxyConnection(Supplier<Connection> connectionSupplier) throws Throwable;

}
