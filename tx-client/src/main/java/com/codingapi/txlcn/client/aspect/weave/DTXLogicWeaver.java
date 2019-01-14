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
package com.codingapi.txlcn.client.aspect.weave;

import com.codingapi.txlcn.client.aspect.BusinessCallback;
import com.codingapi.txlcn.client.bean.DTXInfo;
import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.client.bean.TxTransactionInfo;
import com.codingapi.txlcn.spi.sleuth.TracerHelper;
import com.codingapi.txlcn.client.support.TXLCNTransactionServiceExecutor;
import com.codingapi.txlcn.commons.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private final TXLCNTransactionServiceExecutor transactionServiceExecutor;

    @Autowired
    public DTXLogicWeaver(TracerHelper tracerHelper,
                          TXLCNTransactionServiceExecutor transactionServiceExecutor) {
        this.tracerHelper = tracerHelper;
        this.transactionServiceExecutor = transactionServiceExecutor;
    }

    public Object runTransaction(DTXInfo dtxInfo, BusinessCallback business) throws Throwable {

        if (Objects.isNull(DTXLocal.cur())) {
            DTXLocal.getOrNew();
        } else {
            return business.call();
        }

        log.info("tx-unit start---->");
        // 事务发起方判断
        boolean isTransactionStart = tracerHelper.getGroupId() == null;

        // 该线程事务
        String groupId = isTransactionStart ? RandomUtils.randomKey() : tracerHelper.getGroupId();
        String unitId = dtxInfo.getUnitId();
        DTXLocal dtxLocal = DTXLocal.getOrNew();
        if (dtxLocal.getUnitId() != null) {
            dtxLocal.setInUnit(true);
            log.info("tx > unit[{}] in unit: {}", unitId, dtxLocal.getUnitId());
        }
        dtxLocal.setUnitId(unitId);
        dtxLocal.setGroupId(groupId);
        dtxLocal.setTransactionType(dtxInfo.getTransactionType());

        // 事务参数
        TxTransactionInfo info = new TxTransactionInfo(dtxInfo.getTransactionType(), isTransactionStart,
                groupId, unitId, dtxInfo.getTransactionInfo(), business,
                dtxInfo.getBusinessMethod(), dtxInfo.getTransactionPropagation());

        //LCN事务处理器
        try {
            return transactionServiceExecutor.transactionRunning(info);
        } finally {
            synchronized (DTXLocal.cur()) {
                DTXLocal.cur().notifyAll();
            }
            DTXLocal.makeNeverAppeared();
            log.info("tx-unit end------>");
        }
    }
}
