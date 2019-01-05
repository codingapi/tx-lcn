package com.codingapi.tx.client.aspect.transaction;

import com.codingapi.tx.client.bean.DTXLocal;
import com.codingapi.tx.spi.sleuth.TracerHelper;
import com.codingapi.tx.commons.annotation.TxTransaction;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.support.separate.TXLCNTransactionServiceExecutor;
import com.codingapi.tx.commons.util.Transactions;
import com.codingapi.tx.commons.bean.TransactionInfo;
import com.codingapi.tx.commons.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
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
public class AspectBeforeServiceExecutor {

    private final TracerHelper tracerHelper;

    private final TXLCNTransactionServiceExecutor transactionServiceExecutor;

    @Autowired
    public AspectBeforeServiceExecutor(TracerHelper tracerHelper,
                                       TXLCNTransactionServiceExecutor transactionServiceExecutor) {
        this.tracerHelper = tracerHelper;
        this.transactionServiceExecutor = transactionServiceExecutor;
    }

    Object runTransaction(String transactionType, ProceedingJoinPoint point) throws Throwable {
        log.info("TX-LCN local start---->");
        // build TransactionInfo 获取切面方法参数
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = point.getTarget().getClass();
        Object[] parameters = point.getArgs();
        Method thisMethod = clazz.getMethod(method.getName(), method.getParameterTypes());

        if (Objects.isNull(transactionType)){
            TxTransaction transaction = thisMethod.getAnnotation(TxTransaction.class);
            assert Objects.nonNull(transaction);
            transactionType = transaction.type();
        }

        TransactionInfo transactionInfo = new TransactionInfo(clazz,
                thisMethod.getName(),
                thisMethod.toString(),
                parameters,
                method.getParameterTypes());

        // 事务发起方判断
        boolean isTransactionStart = tracerHelper.getGroupId() == null;


        // 该线程事务
        String groupId = isTransactionStart ? RandomUtils.randomKey() : tracerHelper.getGroupId();
        String unitId = Transactions.unitId(transactionInfo.getMethodStr());
        DTXLocal dtxLocal = DTXLocal.getOrNew();
        if (dtxLocal.getUnitId() != null) {
            dtxLocal.setInUnit(true);
            log.info("tx > unit[{}] in unit: {}", unitId, dtxLocal.getUnitId());
        }
        dtxLocal.setUnitId(unitId);
        dtxLocal.setGroupId(groupId);
        dtxLocal.setTransactionType(transactionType);

        // 事务参数
        TxTransactionInfo info = new TxTransactionInfo(
                transactionType,
                isTransactionStart,
                groupId,
                unitId,
                transactionInfo,
                point,
                thisMethod);

        //LCN事务处理器
        try {
            return transactionServiceExecutor.transactionRunning(info);
        } finally {
            DTXLocal.makeNeverAppeared();
            log.info("TX-LCN local end------>");
        }
    }
}
