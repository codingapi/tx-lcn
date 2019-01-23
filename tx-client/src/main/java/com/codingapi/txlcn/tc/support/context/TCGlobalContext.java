package com.codingapi.txlcn.tc.support.context;

import com.codingapi.txlcn.commons.exception.TCGlobalContextException;
import com.codingapi.txlcn.commons.util.function.ThrowableSupplier;
import com.codingapi.txlcn.tc.core.TccTransactionInfo;
import com.codingapi.txlcn.tc.core.lcn.resource.LcnConnectionProxy;

import java.sql.Connection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Description:
 * Date: 19-1-22 下午6:13
 *
 * @author ujued
 */
public interface TCGlobalContext {

    void setLcnConnection(String groupId, LcnConnectionProxy connectionProxy);

    LcnConnectionProxy getLcnConnection(String groupId) throws TCGlobalContextException;


    TccTransactionInfo tccTransactionInfo(String unitId, ThrowableSupplier<TccTransactionInfo> supplier) throws Throwable;


    void addTxcLockId(String groupId, String unitId, Set<String> lockIdSet);

    Set<String> findTxcLockSet(String groupId, String unitId) throws TCGlobalContextException;

    void newDTXContext(String groupId);

    void destroyDTXContext(String groupId);

    DTXContext dtxContext(String groupId);

    void clearGroup(String groupId);
}
