package com.codingapi.txlcn.client.core.lcn.resource;

import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.client.support.resouce.TransactionResourceExecutor;
import com.codingapi.txlcn.client.support.common.cache.TransactionAttachmentCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.function.Supplier;

/**
 * @author lorne
 * @date 2018/12/2
 * @description
 */
@Service(value = "transaction_lcn")
@Slf4j
public class LCNTransactionResourceExecutor implements TransactionResourceExecutor {


    private final TransactionAttachmentCache transactionAttachmentCache;

    @Autowired
    public LCNTransactionResourceExecutor(TransactionAttachmentCache transactionAttachmentCache) {
        this.transactionAttachmentCache = transactionAttachmentCache;
    }

    @Override
    public Connection proxyConnection(Supplier<Connection> connectionSupplier) throws Throwable {
        String groupId = DTXLocal.cur().getGroupId();
        String unitId = DTXLocal.cur().getUnitId();
        Connection connection = transactionAttachmentCache.attachment(
                groupId, unitId, LCNConnectionProxy.class, () -> new LCNConnectionProxy(connectionSupplier.get())
        );
        connection.setAutoCommit(false);
        return connection;
    }
}
