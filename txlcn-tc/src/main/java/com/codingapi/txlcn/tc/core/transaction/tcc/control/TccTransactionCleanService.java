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
package com.codingapi.txlcn.tc.core.transaction.tcc.control;

import com.codingapi.txlcn.common.exception.TransactionClearException;
import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.TccTransactionInfo;
import com.codingapi.txlcn.tc.core.TransactionCleanService;
import com.codingapi.txlcn.tc.core.context.TCGlobalContext;
import com.codingapi.txlcn.tc.txmsg.TxMangerReporter;
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

    private final ApplicationContext applicationContext;

    private final TxMangerReporter txMangerReporter;

    private final TCGlobalContext globalContext;

    @Autowired
    public TccTransactionCleanService(ApplicationContext applicationContext,
                                      TxMangerReporter txMangerReporter, TCGlobalContext globalContext) {
        this.applicationContext = applicationContext;
        this.txMangerReporter = txMangerReporter;
        this.globalContext = globalContext;
    }

    @Override
    public void clear(String groupId, int state, String unitId, String unitType) throws TransactionClearException {
        Method exeMethod;
        try {
            TccTransactionInfo tccInfo = globalContext.tccTransactionInfo(unitId, null);
            Object object = applicationContext.getBean(tccInfo.getExecuteClass());
            // 用户的 confirm or cancel method 可以用到这个
            if (Objects.isNull(DTXLocalContext.cur())) {
                DTXLocalContext.getOrNew().setJustNow(true);
            }
            DTXLocalContext.getOrNew().setGroupId(groupId);
            DTXLocalContext.cur().setUnitId(unitId);
            exeMethod = tccInfo.getExecuteClass().getMethod(
                    state == 1 ? tccInfo.getConfirmMethod() : tccInfo.getCancelMethod(),
                    tccInfo.getMethodTypeParameter());
            try {
                exeMethod.invoke(object, tccInfo.getMethodParameter());
                log.debug("User confirm/cancel logic over.");
            } catch (Throwable e) {
                log.error("Tcc clean error.", e);
                txMangerReporter.reportTccCleanException(groupId, unitId, state);
            }
        } catch (Throwable e) {
            throw new TransactionClearException(e.getMessage());
        } finally {
            if (DTXLocalContext.cur().isJustNow()) {
                DTXLocalContext.makeNeverAppeared();
            }
        }
    }
}
