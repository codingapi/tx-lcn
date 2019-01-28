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
package com.codingapi.txlcn.client.core.tcc.control;

import com.codingapi.txlcn.client.bean.DTXLocal;
import com.codingapi.txlcn.client.core.tcc.TccTransactionInfoCache;
import com.codingapi.txlcn.client.message.helper.TxMangerReporter;
import com.codingapi.txlcn.commons.exception.TransactionClearException;
import com.codingapi.txlcn.client.bean.TCCTransactionInfo;
import com.codingapi.txlcn.client.support.cache.TransactionAttachmentCache;
import com.codingapi.txlcn.client.support.TransactionCleanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author 侯存路
 */
@Component
@Slf4j
public class TccTransactionCleanService implements TransactionCleanService {

    private final TransactionAttachmentCache transactionAttachmentCache;

    private final ApplicationContext applicationContext;

    private final TccTransactionInfoCache tccTransactionInfoCache;

    private final TxMangerReporter txMangerReporter;

    @Autowired
    public TccTransactionCleanService(TransactionAttachmentCache transactionAttachmentCache,
                                      ApplicationContext applicationContext,
                                      TccTransactionInfoCache tccTransactionInfoCache,
                                      TxMangerReporter txMangerReporter) {
        this.transactionAttachmentCache = transactionAttachmentCache;
        this.applicationContext = applicationContext;
        this.tccTransactionInfoCache = tccTransactionInfoCache;
        this.txMangerReporter = txMangerReporter;
    }

    @Override
    public void clear(String groupId, int state, String unitId, String unitType) throws TransactionClearException {
        TCCTransactionInfo tccInfo = tccTransactionInfoCache.get(unitId);

        Object object = applicationContext.getBean(tccInfo.getExecuteClass());
        Method exeMethod = null;

        try {
            // 用户的 confirm or cancel method 可以用到这个
            if (Objects.isNull(DTXLocal.cur())) {
                DTXLocal.getOrNew().setJustNow(true);
            }
            DTXLocal.getOrNew().setGroupId(groupId);
            DTXLocal.cur().setUnitId(unitId);
            exeMethod = tccInfo.getExecuteClass().getMethod(
                    state == 1 ? tccInfo.getConfirmMethod() : tccInfo.getCancelMethod(),
                    tccInfo.getMethodTypeParameter());
            try {
                exeMethod.invoke(object, tccInfo.getMethodParameter());
            } catch (Throwable e) {
                log.error("tcc clean error.", e);
                txMangerReporter.reportTccCleanException(groupId, unitId, state);
            }
            // 清理与事务组生命周期一样的资源 see: com.codingapi.txlcn.client.support.TXLCNTransactionServiceExecutor.transactionRunning
            transactionAttachmentCache.removeAttachments(groupId, unitId);
        } catch (Exception e) {
            log.error(" rpc_tcc_" + exeMethod + e.getMessage());
            throw new TransactionClearException(e.getMessage());
        } finally {
            if (DTXLocal.cur().isJustNow()) {
                DTXLocal.makeNeverAppeared();
            }
        }
    }
}
