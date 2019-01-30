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

import com.codingapi.txlcn.common.exception.BeforeBusinessException;
import com.codingapi.txlcn.common.exception.TCGlobalContextException;
import com.codingapi.txlcn.common.util.function.Supplier;
import com.codingapi.txlcn.tc.core.TccTransactionInfo;
import com.codingapi.txlcn.tc.core.lcn.resource.LcnConnectionProxy;
import com.codingapi.txlcn.tc.core.txc.analy.def.PrimaryKeysProvider;
import com.codingapi.txlcn.tc.core.txc.analy.def.bean.TableStruct;
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
 */
@Component
@Slf4j
public class DefaultGlobalContext implements TCGlobalContext {

    private final AttachmentCache attachmentCache;

    private final List<PrimaryKeysProvider> primaryKeysProviders;

    @Autowired
    public DefaultGlobalContext(AttachmentCache attachmentCache,
                                @Autowired(required = false) List<PrimaryKeysProvider> primaryKeysProviders) {
        this.attachmentCache = attachmentCache;
        this.primaryKeysProviders = primaryKeysProviders;
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
    public TccTransactionInfo tccTransactionInfo(String unitId, Supplier<TccTransactionInfo, BeforeBusinessException> supplier)
            throws BeforeBusinessException {
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
    public TableStruct tableStruct(String table, Supplier<TableStruct, SQLException> structSupplier) throws SQLException {
        if (attachmentCache.containsKey("sql.table." + table, TableStruct.class.getName())) {
            log.debug("cache hit! table {}'s struct.", table);
            return attachmentCache.attachment("sql.table." + table, TableStruct.class.getName());
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
        attachmentCache.attach("sql.table." + table, TableStruct.class.getName(), tableStruct);
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
        attachmentCache.attach(txContext.getGroupId() + ".dtx", txContext);
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
    }

    @Override
    public TxContext txContext(String groupId) {
        return attachmentCache.attachment(groupId + ".dtx");
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
        // 销毁GroupId
        TracingContext.tracing().destroy();
    }
}
