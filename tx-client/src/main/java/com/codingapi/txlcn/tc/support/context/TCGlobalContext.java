package com.codingapi.txlcn.tc.support.context;

import com.codingapi.txlcn.commons.util.function.ThrowableSupplier;
import com.codingapi.txlcn.tc.core.TccTransactionInfo;

import java.sql.Connection;
import java.util.function.Supplier;

/**
 * Description:
 * Date: 19-1-22 下午6:13
 *
 * @author ujued
 */
public interface TCGlobalContext {

    Connection lcnConnection(String groupId, String unitId, Supplier<Connection> supplier);

    void removeLcnConnection(String groupId, String unitId);


    TccTransactionInfo tccTransactionInfo(String groupId, String unitId, ThrowableSupplier<TccTransactionInfo> supplier)
            throws Throwable;



    String txcLockId(String groupId, String unitId, Supplier<String> lockId);

    void removeTxcLockId(String groupId, String unitId);



    void newDTXContext(String groupId);

    void destroyDTXContext(String groupId);

    DTXContext dtxContext(String groupId);
}
