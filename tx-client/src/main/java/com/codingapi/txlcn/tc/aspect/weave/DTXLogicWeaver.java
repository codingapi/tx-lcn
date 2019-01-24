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
package com.codingapi.txlcn.tc.aspect.weave;

import com.codingapi.txlcn.commons.util.RandomUtils;
import com.codingapi.txlcn.spi.sleuth.TracerHelper;
import com.codingapi.txlcn.tc.aspect.BusinessCallback;
import com.codingapi.txlcn.tc.core.DTXInfo;
import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.DTXServiceExecutor;
import com.codingapi.txlcn.tc.core.TxTransactionInfo;
import com.codingapi.txlcn.tc.support.context.DTXContext;
import com.codingapi.txlcn.tc.support.context.TCGlobalContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Description:
 * Company: CodingApi
 * Date: 2018/11/29
 *
 * @author ujued
 */
@Component
@Slf4j
public class DTXLogicWeaver {

    private final TracerHelper tracerHelper;

    private final DTXServiceExecutor transactionServiceExecutor;

    private final TCGlobalContext context;

    @Autowired
    public DTXLogicWeaver(TracerHelper tracerHelper, DTXServiceExecutor transactionServiceExecutor, TCGlobalContext context) {
        this.tracerHelper = tracerHelper;
        this.transactionServiceExecutor = transactionServiceExecutor;
        this.context = context;
    }

    public Object runTransaction(DTXInfo dtxInfo, BusinessCallback business) throws Throwable {

        if (Objects.isNull(DTXLocalContext.cur())) {
            DTXLocalContext.getOrNew();
        } else {
            return business.call();
        }

        log.debug("tx-unit start---->");

        // 事务发起方判断
        boolean isTransactionStart = StringUtils.isEmpty(tracerHelper.getGroupId());
        if (isTransactionStart) {
            tracerHelper.createGroupId(RandomUtils.randomKey());
        }
        // 该线程事务
        String groupId = tracerHelper.getGroupId();
        String unitId = dtxInfo.getUnitId();
        DTXLocalContext dtxLocalContext = DTXLocalContext.getOrNew();
        if (dtxLocalContext.getUnitId() != null) {
            dtxLocalContext.setInUnit(true);
            log.info("tx > unit[{}] in unit: {}" , unitId, dtxLocalContext.getUnitId());
        }
        dtxLocalContext.setUnitId(unitId);
        dtxLocalContext.setGroupId(groupId);
        dtxLocalContext.setTransactionType(dtxInfo.getTransactionType());

        // 事务参数
        TxTransactionInfo info = new TxTransactionInfo(dtxInfo.getTransactionType(), isTransactionStart,
                groupId, unitId, dtxInfo.getTransactionInfo(), business,
                dtxInfo.getBusinessMethod(), dtxInfo.getTransactionPropagation());

        //LCN事务处理器
        try {
            context.newDTXContext(groupId);
            return transactionServiceExecutor.transactionRunning(info);
        } finally {
            DTXContext dtxContext = context.dtxContext(info.getGroupId());
            synchronized (dtxContext.getLock()) {
                dtxContext.getLock().notifyAll();
            }
            context.destroyDTXContext(info.getGroupId());
            DTXLocalContext.makeNeverAppeared();
            tracerHelper.createGroupId("");
            log.debug("tx-unit end------>");
        }
    }
}
