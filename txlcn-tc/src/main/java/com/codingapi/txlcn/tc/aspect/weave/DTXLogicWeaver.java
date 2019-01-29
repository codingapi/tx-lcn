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

import com.codingapi.txlcn.tc.aspect.BusinessCallback;
import com.codingapi.txlcn.tc.core.DTXInfo;
import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.DTXServiceExecutor;
import com.codingapi.txlcn.tc.core.context.TCGlobalContext;
import com.codingapi.txlcn.tc.core.context.TxContext;
import com.codingapi.txlcn.tc.core.TxTransactionInfo;
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

    private final DTXServiceExecutor transactionServiceExecutor;

    private final TCGlobalContext globalContext;

    @Autowired
    public DTXLogicWeaver(DTXServiceExecutor transactionServiceExecutor, TCGlobalContext globalContext) {
        this.transactionServiceExecutor = transactionServiceExecutor;
        this.globalContext = globalContext;
    }

    public Object runTransaction(DTXInfo dtxInfo, BusinessCallback business) throws Throwable {

        if (Objects.isNull(DTXLocalContext.cur())) {
            DTXLocalContext.getOrNew();
        } else {
            return business.call();
        }

        log.debug("TX-unit start---->");
        DTXLocalContext dtxLocalContext = DTXLocalContext.getOrNew();
        TxContext txContext;
        if (Objects.isNull(dtxLocalContext.getGroupId())) {
            // 非子Unit开启本地事务上下文
            log.debug("Unit start TxContext.");
            txContext = globalContext.startTx();
            dtxLocalContext.setUnitId(dtxInfo.getUnitId());
            dtxLocalContext.setGroupId(txContext.getGroupId());
            dtxLocalContext.setTransactionType(dtxInfo.getTransactionType());
        } else {
            // 子Unit获取父上下文
            log.debug("Unit[{}] in unit: {}, use parent's TxContext.", dtxInfo.getUnitId(), dtxLocalContext.getUnitId());
            txContext = globalContext.txContext(dtxLocalContext.getGroupId());
            dtxLocalContext.setInUnit(true);
        }

        // 事务参数
        TxTransactionInfo info = new TxTransactionInfo();
        info.setBusinessCallback(business);
        info.setGroupId(txContext.getGroupId());
        info.setUnitId(dtxInfo.getUnitId());
        info.setPointMethod(dtxInfo.getBusinessMethod());
        info.setPropagation(dtxInfo.getTransactionPropagation());
        info.setTransactionInfo(dtxInfo.getTransactionInfo());
        info.setTransactionType(dtxInfo.getTransactionType());
        info.setTransactionStart(txContext.isDtxStart());

        //LCN事务处理器
        try {
            return transactionServiceExecutor.transactionRunning(info);
        } finally {
            if (!dtxLocalContext.isInUnit()) {
                log.debug("Destroy TxContext[{}]", info.getGroupId());
                // 获取事务上下文通知事务执行完毕
                synchronized (txContext.getLock()) {
                    txContext.getLock().notifyAll();
                }
                // 销毁事务
                globalContext.destroyTx(info.getGroupId());
                DTXLocalContext.makeNeverAppeared();
            }
            log.debug("TX-unit end------>");
        }
    }
}
