package com.codingapi.txlcn.tc.support.context;

import com.codingapi.txlcn.commons.util.function.ThrowableSupplier;
import com.codingapi.txlcn.tc.core.TccTransactionInfo;
import com.codingapi.txlcn.tc.core.lcn.resource.LcnConnectionProxy;
import com.codingapi.txlcn.tc.core.tcc.TccTransactionInfoCache;
import com.codingapi.txlcn.tc.support.cache.TransactionAttachmentCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.function.Supplier;

/**
 * Description:
 * Date: 19-1-22 下午6:17
 *
 * @author ujued
 */
@Component
public class DefaultGlobalContext implements TCGlobalContext {

    private final TransactionAttachmentCache transactionAttachmentCache;

    private final TccTransactionInfoCache tccTransactionInfoCache;

    @Autowired
    public DefaultGlobalContext(TransactionAttachmentCache transactionAttachmentCache, TccTransactionInfoCache tccTransactionInfoCache) {
        this.transactionAttachmentCache = transactionAttachmentCache;
        this.tccTransactionInfoCache = tccTransactionInfoCache;
    }

    @Override
    public Connection lcnConnection(String groupId, String unitId, Supplier<Connection> supplier) {
        return transactionAttachmentCache.attachment(
                groupId, unitId, LcnConnectionProxy.class, () -> new LcnConnectionProxy(supplier.get())
        );
    }

    @Override
    public void removeLcnConnection(String groupId, String unitId) {
        transactionAttachmentCache.removeAttachments(groupId, unitId);
    }

    @Override
    public TccTransactionInfo tccTransactionInfo(String groupId, String unitId, ThrowableSupplier<TccTransactionInfo> supplier)
            throws Throwable {
        if (tccTransactionInfoCache.get(groupId + unitId) == null) {
            tccTransactionInfoCache.putIfAbsent(groupId + unitId, supplier.get());
        }
        return tccTransactionInfoCache.get(groupId + unitId);
    }

    @Override
    public String txcLockId(String groupId, String unitId, Supplier<String> lockId) {
        return null;
    }

    @Override
    public void removeTxcLockId(String groupId, String unitId) {

    }

    @Override
    public void newDTXContext(String groupId) {
        transactionAttachmentCache.setContext(groupId, new DTXContext());
        dtxContext(groupId);
    }

    @Override
    public void destroyDTXContext(String groupId) {
        transactionAttachmentCache.destroyContext(groupId);
    }

    @Override
    public DTXContext dtxContext(String groupId) {
        return transactionAttachmentCache.context(groupId);
    }
}
