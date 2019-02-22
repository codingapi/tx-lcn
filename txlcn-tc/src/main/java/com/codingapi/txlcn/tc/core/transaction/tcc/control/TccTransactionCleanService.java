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
import com.codingapi.txlcn.common.util.Maps;
import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.TccTransactionInfo;
import com.codingapi.txlcn.tc.core.TransactionCleanService;
import com.codingapi.txlcn.tc.core.context.TCGlobalContext;
import com.codingapi.txlcn.tc.txmsg.TMReporter;
import com.codingapi.txlcn.tracing.TracingConstants;
import com.codingapi.txlcn.tracing.TracingContext;
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

    private final TMReporter tmReporter;

    private final TCGlobalContext globalContext;

    @Autowired
    public TccTransactionCleanService(ApplicationContext applicationContext,
                                      TMReporter tmReporter, TCGlobalContext globalContext) {
        this.applicationContext = applicationContext;
        this.tmReporter = tmReporter;
        this.globalContext = globalContext;
    }

    @Override
    public void clear(String groupId, int state, String unitId, String unitType) throws TransactionClearException {
        Method exeMethod;
        boolean shouldDestroy = !TracingContext.tracing().hasGroup();
        try {
            TccTransactionInfo tccInfo = globalContext.tccTransactionInfo(unitId, null);
            Object object = applicationContext.getBean(tccInfo.getExecuteClass());
            // 将要移除。
            if (Objects.isNull(DTXLocalContext.cur())) {
                DTXLocalContext.getOrNew().setJustNow(true);
            }
            if (shouldDestroy) {
                TracingContext.init(Maps.of(TracingConstants.GROUP_ID, groupId, TracingConstants.APP_MAP, "{}"));
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
                tmReporter.reportTccCleanException(groupId, unitId, state);
            }
        } catch (Throwable e) {
            throw new TransactionClearException(e.getMessage());
        } finally {
            if (DTXLocalContext.cur().isJustNow()) {
                DTXLocalContext.makeNeverAppeared();
            }
            if (shouldDestroy) {
                TracingContext.tracing().destroy();
            }
        }
    }
}
