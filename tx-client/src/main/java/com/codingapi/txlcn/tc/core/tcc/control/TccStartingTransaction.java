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
package com.codingapi.txlcn.tc.core.tcc.control;

import com.codingapi.txlcn.commons.annotation.TccTransaction;
import com.codingapi.txlcn.commons.exception.BeforeBusinessException;
import com.codingapi.txlcn.tc.core.DTXLocalContext;
import com.codingapi.txlcn.tc.core.DTXLocalControl;
import com.codingapi.txlcn.tc.core.TccTransactionInfo;
import com.codingapi.txlcn.tc.core.TxTransactionInfo;
import com.codingapi.txlcn.tc.core.context.TCGlobalContext;
import com.codingapi.txlcn.tc.support.template.TransactionControlTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author 侯存路
 */
@Service(value = "control_tcc_starting")
@Slf4j
public class TccStartingTransaction implements DTXLocalControl {

    private final TransactionControlTemplate transactionControlTemplate;

    private final TCGlobalContext context;

    @Autowired
    public TccStartingTransaction(TransactionControlTemplate transactionControlTemplate,
                                  TCGlobalContext context) {
        this.transactionControlTemplate = transactionControlTemplate;
        this.context = context;
    }

    static TccTransactionInfo prepareTccInfo(TxTransactionInfo info) throws BeforeBusinessException {
        Method method = info.getPointMethod();
        TccTransaction tccTransaction = method.getAnnotation(TccTransaction.class);
        if (tccTransaction == null) {
            throw new BeforeBusinessException("TCC type need @TccTransaction in " + method.getName());
        }
        String cancelMethod = tccTransaction.cancelMethod();
        String confirmMethod = tccTransaction.confirmMethod();
        Class<?> executeClass = tccTransaction.executeClass();
        if (StringUtils.isEmpty(tccTransaction.cancelMethod())) {
            cancelMethod = "cancel" + StringUtils.capitalize(method.getName());
        }
        if (StringUtils.isEmpty(tccTransaction.confirmMethod())) {
            confirmMethod = "confirm" + StringUtils.capitalize(method.getName());
        }
        if (Void.class.isAssignableFrom(executeClass)) {
            executeClass = info.getTransactionInfo().getTargetClazz();
        }

        TccTransactionInfo tccInfo = new TccTransactionInfo();
        tccInfo.setExecuteClass(executeClass);
        tccInfo.setCancelMethod(cancelMethod);
        tccInfo.setConfirmMethod(confirmMethod);
        tccInfo.setMethodParameter(info.getTransactionInfo().getArgumentValues());
        tccInfo.setMethodTypeParameter(info.getTransactionInfo().getParameterTypes());

        return tccInfo;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) throws BeforeBusinessException {
        // cache tcc info
        try {
            context.tccTransactionInfo(info.getUnitId(), () -> prepareTccInfo(info))
                    .setMethodParameter(info.getTransactionInfo().getArgumentValues());
        } catch (Throwable throwable) {
            throw new BeforeBusinessException(throwable);
        }

        // create DTX group
        transactionControlTemplate.createGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionInfo(), info.getTransactionType());
    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        DTXLocalContext.cur().setSysTransactionState(0);
    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) {
        DTXLocalContext.cur().setSysTransactionState(1);
    }

    /**
     * 事务发起方 自己执行  提交 / 取消 事件
     *
     * @param info info
     */
    @Override
    public void postBusinessCode(TxTransactionInfo info) {
        transactionControlTemplate.notifyGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionType(), DTXLocalContext.transactionState());
    }
}
