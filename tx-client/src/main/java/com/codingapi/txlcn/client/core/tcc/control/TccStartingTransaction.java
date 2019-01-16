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
import com.codingapi.txlcn.client.bean.TCCTransactionInfo;
import com.codingapi.txlcn.client.bean.TxTransactionInfo;
import com.codingapi.txlcn.client.core.tcc.TccTransactionInfoCache;
import com.codingapi.txlcn.client.support.TXLCNTransactionControl;
import com.codingapi.txlcn.client.support.common.template.TransactionControlTemplate;
import com.codingapi.txlcn.commons.annotation.TccTransaction;
import com.codingapi.txlcn.commons.exception.BeforeBusinessException;
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
public class TccStartingTransaction implements TXLCNTransactionControl {

    private final TransactionControlTemplate transactionControlTemplate;

    private final TccTransactionInfoCache tccTransactionInfoCache;

    @Autowired
    public TccStartingTransaction(TransactionControlTemplate transactionControlTemplate,
                                  TccTransactionInfoCache tccTransactionInfoCache) {
        this.transactionControlTemplate = transactionControlTemplate;
        this.tccTransactionInfoCache = tccTransactionInfoCache;
    }

    static TCCTransactionInfo prepareTccInfo(TxTransactionInfo info) throws BeforeBusinessException {
        Method method = info.getPointMethod();
        TccTransaction tccTransaction = method.getAnnotation(TccTransaction.class);
        if (tccTransaction == null) {
            throw new BeforeBusinessException("TCC模式下需添加 @TccTransaction 注解在 " + method.getName() + " 上 ");
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

        TCCTransactionInfo tccInfo = new TCCTransactionInfo();
        tccInfo.setExecuteClass(executeClass);
        tccInfo.setCancelMethod(cancelMethod);
        tccInfo.setConfirmMethod(confirmMethod);
        tccInfo.setMethodParameter(info.getTransactionInfo().getArgumentValues());
        tccInfo.setMethodTypeParameter(info.getTransactionInfo().getParameterTypes());

        return tccInfo;
    }

    @Override
    public void preBusinessCode(TxTransactionInfo info) throws BeforeBusinessException {
        // 缓存TCC事务信息，如果有必要
        if (tccTransactionInfoCache.get(info.getUnitId()) == null) {
            tccTransactionInfoCache.putIfAbsent(info.getUnitId(), prepareTccInfo(info));
        }

        tccTransactionInfoCache.get(info.getUnitId()).setMethodParameter(info.getTransactionInfo().getArgumentValues());

        // 创建事务组
        transactionControlTemplate.createGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionInfo(), info.getTransactionType());
    }

    @Override
    public void onBusinessCodeError(TxTransactionInfo info, Throwable throwable) {
        DTXLocal.currentDTX().setTransactionState(0);
    }

    @Override
    public void onBusinessCodeSuccess(TxTransactionInfo info, Object result) {
        DTXLocal.currentDTX().setTransactionState(1);
    }

    /**
     * 事务发起方 自己执行  提交 / 取消 事件
     *
     * @param info info
     */
    @Override
    public void postBusinessCode(TxTransactionInfo info) {
        transactionControlTemplate.notifyGroup(
                info.getGroupId(), info.getUnitId(), info.getTransactionType(), DTXLocal.currentDTX().transactionState());
    }
}
