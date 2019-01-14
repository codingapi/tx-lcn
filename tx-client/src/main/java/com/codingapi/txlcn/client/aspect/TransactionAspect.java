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
package com.codingapi.txlcn.client.aspect;

import com.codingapi.txlcn.client.aspect.weave.DTXLogicWeaver;
import com.codingapi.txlcn.client.bean.DTXInfo;
import com.codingapi.txlcn.client.config.TxClientConfig;
import com.codingapi.txlcn.client.support.common.DTXInfoPool;
import com.codingapi.txlcn.commons.annotation.*;
import com.codingapi.txlcn.commons.util.Transactions;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * LCN 事务拦截器
 * create by lorne on 2018/1/5
 */
@Aspect
@Component
@Slf4j
public class TransactionAspect implements Ordered {

    private final TxClientConfig txClientConfig;

    private final com.codingapi.txlcn.client.aspect.weave.DTXLogicWeaver DTXLogicWeaver;

    @Autowired
    public TransactionAspect(TxClientConfig txClientConfig, DTXLogicWeaver DTXLogicWeaver) {
        this.txClientConfig = txClientConfig;
        this.DTXLogicWeaver = DTXLogicWeaver;
    }

    /**
     * 分布式事务切点描述
     */
    @Pointcut("@annotation(com.codingapi.txlcn.commons.annotation.TxTransaction)")
    public void txTransactionPointcut() {
    }

    /**
     * 分布式事务切点描述 (Type of LCN)
     */
    @Pointcut("@annotation(com.codingapi.txlcn.commons.annotation.LcnTransaction)")
    public void lcnTransactionPointcut() {
    }

    /**
     * 分布式事务切点描述 (Type of TXC)
     */
    @Pointcut("@annotation(com.codingapi.txlcn.commons.annotation.TxcTransaction)")
    public void txcTransactionPointcut() {
    }

    /**
     * 分布式事务切点描述 (Type of TCC)
     */
    @Pointcut("@annotation(com.codingapi.txlcn.commons.annotation.TccTransaction)")
    public void tccTransactionPointcut() {
    }


    @Around("txTransactionPointcut()")
    public Object transactionRunning(ProceedingJoinPoint point) throws Throwable {
        DTXInfo dtxInfo = DTXInfoPool.get(point);
        TxTransaction txTransaction = dtxInfo.getBusinessMethod().getAnnotation(TxTransaction.class);
        dtxInfo.setTransactionType(txTransaction.type());
        dtxInfo.setTransactionPropagation(txTransaction.dtxp());
        return DTXLogicWeaver.runTransaction(dtxInfo, point::proceed);
    }

    @Around("lcnTransactionPointcut() && !txcTransactionPointcut()" +
            "&& !tccTransactionPointcut() && !txTransactionPointcut()")
    public Object runWithLcnTransaction(ProceedingJoinPoint point) throws Throwable {
        DTXInfo dtxInfo = DTXInfoPool.get(point);
        LcnTransaction lcnTransaction = dtxInfo.getBusinessMethod().getAnnotation(LcnTransaction.class);
        dtxInfo.setTransactionType(Transactions.LCN);
        dtxInfo.setTransactionPropagation(lcnTransaction.dtxp());
        return DTXLogicWeaver.runTransaction(dtxInfo, point::proceed);
    }

    @Around("txcTransactionPointcut() && !lcnTransactionPointcut()" +
            "&& !tccTransactionPointcut() && !txTransactionPointcut()")
    public Object runWithTxcTransaction(ProceedingJoinPoint point) throws Throwable {
        DTXInfo dtxInfo = DTXInfoPool.get(point);
        TxcTransaction txcTransaction = dtxInfo.getBusinessMethod().getAnnotation(TxcTransaction.class);
        dtxInfo.setTransactionType(Transactions.TXC);
        dtxInfo.setTransactionPropagation(txcTransaction.dtxp());
        return DTXLogicWeaver.runTransaction(dtxInfo, point::proceed);
    }

    @Around("tccTransactionPointcut() && !lcnTransactionPointcut()" +
            "&& !txcTransactionPointcut() && !txTransactionPointcut()")
    public Object runWithTccTransaction(ProceedingJoinPoint point) throws Throwable {
        DTXInfo dtxInfo = DTXInfoPool.get(point);
        TccTransaction tccTransaction = dtxInfo.getBusinessMethod().getAnnotation(TccTransaction.class);
        dtxInfo.setTransactionType(Transactions.TCC);
        dtxInfo.setTransactionPropagation(tccTransaction.dtxp());
        return DTXLogicWeaver.runTransaction(dtxInfo, point::proceed);
    }

    @Around("this(com.codingapi.txlcn.commons.annotation.ITxTransaction) && execution( * *(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        if (!(point.getThis() instanceof ITxTransaction)) {
            throw new IllegalStateException("error join point");
        }
        DTXInfo dtxInfo = DTXInfoPool.get(point);
        ITxTransaction txTransaction = (ITxTransaction) point.getThis();
        dtxInfo.setTransactionType(txTransaction.transactionType());
        dtxInfo.setTransactionPropagation(DTXPropagation.REQUIRED);
        return DTXLogicWeaver.runTransaction(dtxInfo, point::proceed);
    }

    @Override
    public int getOrder() {
        return Objects.isNull(txClientConfig.getDtxAspectOrder()) ?
                txClientConfig.getControlOrder() : txClientConfig.getDtxAspectOrder();
    }

}
