package com.codingapi.txlcn.tc.support.context;

import com.codingapi.txlcn.commons.exception.TCGlobalContextException;
import com.codingapi.txlcn.commons.util.function.ThrowableSupplier;
import com.codingapi.txlcn.tc.core.TccTransactionInfo;
import com.codingapi.txlcn.tc.core.lcn.resource.LcnConnectionProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Description:
 * Date: 19-1-22 下午6:17
 *
 * @author ujued
 */
@Component
public class DefaultGlobalContext implements TCGlobalContext {

    private final AttachmentCache attachmentCache;

    @Autowired
    public DefaultGlobalContext(AttachmentCache attachmentCache) {
        this.attachmentCache = attachmentCache;
    }

    @Override
    public void setLcnConnection(String groupId, LcnConnectionProxy connectionProxy) {
        attachmentCache.attach(groupId, LcnConnectionProxy.class.getName(), connectionProxy);
    }

    @Override
    public LcnConnectionProxy getLcnConnection(String groupId)
            throws TCGlobalContextException {
        if (attachmentCache.containsKey(groupId, LcnConnectionProxy.class.getName())) {
            return attachmentCache.attachment(groupId, LcnConnectionProxy.class.getName());
        }
        throw new TCGlobalContextException("non exists lcn connection");
    }

    @Override
    public TccTransactionInfo tccTransactionInfo(String unitId, ThrowableSupplier<TccTransactionInfo> supplier) throws Throwable {
        if (Objects.isNull(supplier)) {
            return attachmentCache.attachment(unitId, TccTransactionInfo.class.getName());
        }

        if (attachmentCache.containsKey(unitId, TccTransactionInfo.class.getName())) {
            return attachmentCache.attachment(unitId, TccTransactionInfo.class.getName());
        }

        TccTransactionInfo tccTransactionInfo = supplier.get();
        attachmentCache.attach(unitId, TccTransactionInfo.class.getName(), tccTransactionInfo);
        return tccTransactionInfo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addTxcLockId(String groupId, String unitId, Set<String> lockIdList) {
        String lockPrefix = "txc.lock." + unitId;
        if (attachmentCache.containsKey(groupId, lockPrefix)) {
            ((Set) attachmentCache.attachment(groupId, lockPrefix)).addAll(lockIdList);
            return;
        }
        Set<String> lockList = new HashSet<>(lockIdList);
        attachmentCache.attach(groupId, lockPrefix, lockList);
    }

    @Override
    public Set<String> findTxcLockSet(String groupId, String unitId) throws TCGlobalContextException {
        if (attachmentCache.containsKey(groupId, "txc.lock." + unitId)) {
            return attachmentCache.attachment(groupId, "txc.lock." + unitId);
        }
        throw new TCGlobalContextException("non exists lock id.");
    }

    @Override
    public void newDTXContext(String groupId) {
        attachmentCache.attach(groupId + ".dtx", "dtx.context", new DTXContext());
        dtxContext(groupId);
    }

    /**
     * 在用户业务前生成，业务后销毁
     *
     * @param groupId
     */
    @Override
    public void destroyDTXContext(String groupId) {
        attachmentCache.remove(groupId + ".dtx", "dtx.context");
    }

    @Override
    public DTXContext dtxContext(String groupId) {
        return attachmentCache.attachment(groupId + ".dtx", "dtx.context");
    }

    /**
     * 清理事务时调用
     *
     * @param groupId groupId
     */
    @Override
    public void clearGroup(String groupId) {
        this.attachmentCache.remove(groupId);
    }
}
