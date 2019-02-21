/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.tc.core.context;

import com.codingapi.txlcn.common.exception.TCGlobalContextException;
import com.codingapi.txlcn.common.exception.TransactionException;
import com.codingapi.txlcn.common.util.function.Supplier;
import com.codingapi.txlcn.tc.config.TxClientConfig;
import com.codingapi.txlcn.tc.core.TccTransactionInfo;
import com.codingapi.txlcn.tc.core.transaction.lcn.resource.LcnConnectionProxy;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.PrimaryKeysProvider;
import com.codingapi.txlcn.tc.core.transaction.txc.analy.def.bean.TableStruct;
import com.codingapi.txlcn.tracing.TracingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Description:
 * Date: 19-1-22 下午6:17
 *
 * @author ujued
 * @see AttachmentCache
 * @see PrimaryKeysProvider
 */
@Component
@Slf4j
public class DefaultGlobalContext implements TCGlobalContext {

    private final AttachmentCache attachmentCache;

    private final List<PrimaryKeysProvider> primaryKeysProviders;

    private final TxClientConfig clientConfig;

    @Autowired
    public DefaultGlobalContext(AttachmentCache attachmentCache, TxClientConfig clientConfig,
                                @Autowired(required = false) List<PrimaryKeysProvider> primaryKeysProviders) {
        this.attachmentCache = attachmentCache;
        this.primaryKeysProviders = primaryKeysProviders;
        this.clientConfig = clientConfig;
    }

    @Override
    public void setLcnConnection(String groupId, LcnConnectionProxy connectionProxy) {
        attachmentCache.attach(groupId, LcnConnectionProxy.class.getName(), connectionProxy);
    }

    @Override
    public LcnConnectionProxy getLcnConnection(String groupId) throws TCGlobalContextException {
        if (attachmentCache.containsKey(groupId, LcnConnectionProxy.class.getName())) {
            return attachmentCache.attachment(groupId, LcnConnectionProxy.class.getName());
        }
        throw new TCGlobalContextException("non exists lcn connection");
    }

    @Override
    public TccTransactionInfo tccTransactionInfo(String unitId, Supplier<TccTransactionInfo, TransactionException> supplier)
            throws TransactionException {
        String unitTransactionInfoKey = unitId + ".tcc.transaction";
        if (Objects.isNull(supplier)) {
            return attachmentCache.attachment(unitTransactionInfoKey);
        }

        if (attachmentCache.containsKey(unitTransactionInfoKey)) {
            return attachmentCache.attachment(unitTransactionInfoKey);
        }

        TccTransactionInfo tccTransactionInfo = supplier.get();
        attachmentCache.attach(unitTransactionInfoKey, tccTransactionInfo);
        return tccTransactionInfo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addTxcLockId(String groupId, String unitId, Set<String> lockIdList) {
        String lockKey = unitId + ".txc.lock";
        if (attachmentCache.containsKey(groupId, lockKey)) {
            ((Set) attachmentCache.attachment(groupId, lockKey)).addAll(lockIdList);
            return;
        }
        Set<String> lockList = new HashSet<>(lockIdList);
        attachmentCache.attach(groupId, lockKey, lockList);
    }

    @Override
    public Set<String> findTxcLockSet(String groupId, String unitId) throws TCGlobalContextException {
        String lockKey = unitId + ".txc.lock";
        if (attachmentCache.containsKey(groupId, lockKey)) {
            return attachmentCache.attachment(groupId, lockKey);
        }
        throw new TCGlobalContextException("non exists lock id.");
    }

    @Override
    public TableStruct tableStruct(String table, Supplier<TableStruct, SQLException> structSupplier) throws SQLException {
        String tableStructKey = table + ".struct";
        if (attachmentCache.containsKey(tableStructKey)) {
            log.debug("cache hit! table {}'s struct.", table);
            return attachmentCache.attachment(tableStructKey);
        }
        TableStruct tableStruct = structSupplier.get();
        if (Objects.nonNull(primaryKeysProviders)) {
            primaryKeysProviders.forEach(primaryKeysProvider -> {
                List<String> users = primaryKeysProvider.provide().get(table);
                if (Objects.nonNull(users)) {
                    List<String> primaryKes = tableStruct.getPrimaryKeys();
                    primaryKes.addAll(users.stream()
                            .filter(key -> !primaryKes.contains(key))
                            .filter(key -> tableStruct.getColumns().keySet().contains(key)).collect(Collectors.toList()));
                    tableStruct.setPrimaryKeys(primaryKes);
                }
            });
        }
        attachmentCache.attach(tableStructKey, tableStruct);
        return tableStruct;
    }

    @Override
    public TxContext startTx() {
        TxContext txContext = new TxContext();
        // 事务发起方判断
        txContext.setDtxStart(!TracingContext.tracing().hasGroup());
        if (txContext.isDtxStart()) {
            TracingContext.tracing().beginTransactionGroup();
        }
        txContext.setGroupId(TracingContext.tracing().groupId());
        String txContextKey = txContext.getGroupId() + ".dtx";
        attachmentCache.attach(txContextKey, txContext);
        log.debug("Start TxContext[{}]", txContext.getGroupId());
        return txContext;
    }

    /**
     * 在用户业务前生成，业务后销毁
     *
     * @param groupId groupId
     */
    @Override
    public void destroyTx(String groupId) {
        attachmentCache.remove(groupId + ".dtx");
        log.debug("Destroy TxContext[{}]", groupId);
    }

    @Override
    public TxContext txContext(String groupId) {
        return attachmentCache.attachment(groupId + ".dtx");
    }

    @Override
    public TxContext txContext() {
        return txContext(TracingContext.tracing().groupId());
    }

    @Override
    public void destroyTx() {
        if (!hasTxContext()) {
            throw new IllegalStateException("non TxContext.");
        }
        destroyTx(txContext().getGroupId());
    }

    @Override
    public boolean hasTxContext() {
        return TracingContext.tracing().hasGroup() && txContext(TracingContext.tracing().groupId()) != null;
    }

    @Override
    public boolean isDTXTimeout() {
        if (!hasTxContext()) {
            throw new IllegalStateException("non TxContext.");
        }
        return (System.currentTimeMillis() - txContext().getCreateTime()) >= clientConfig.getDtxTime();
    }

    @Override
    public int dtxState(String groupId) {
        return this.attachmentCache.containsKey(groupId, "rollback-only") ? 0 : 1;
    }

    @Override
    public void setRollbackOnly(String groupId) {
        this.attachmentCache.attach(groupId, "rollback-only", true);
    }

    /**
     * 清理事务时调用
     *
     * @param groupId groupId
     */
    @Override
    public void clearGroup(String groupId) {
        // 事务组相关的数据
        this.attachmentCache.removeAll(groupId);
    }
}
