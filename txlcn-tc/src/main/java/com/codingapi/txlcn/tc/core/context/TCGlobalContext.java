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
import com.codingapi.txlcn.tc.core.txc.analy.def.bean.TableStruct;

import java.sql.SQLException;
import java.util.Set;

/**
 * Description:
 * Date: 19-1-22 下午6:13
 *
 * @author ujued
 */
public interface TCGlobalContext {

    void setLcnConnection(String groupId, LcnConnectionProxy connectionProxy);

    LcnConnectionProxy getLcnConnection(String groupId) throws TCGlobalContextException;


    TccTransactionInfo tccTransactionInfo(String unitId, Supplier<TccTransactionInfo, BeforeBusinessException> supplier)
            throws BeforeBusinessException;


    void addTxcLockId(String groupId, String unitId, Set<String> lockIdSet);

    Set<String> findTxcLockSet(String groupId, String unitId) throws TCGlobalContextException;

    TableStruct tableStruct(String table, Supplier<TableStruct, SQLException> structSupplier) throws SQLException;

    void clearGroup(String groupId);

    TxContext startTx();

    void destroyTx(String groupId);

    TxContext txContext(String groupId);
}
