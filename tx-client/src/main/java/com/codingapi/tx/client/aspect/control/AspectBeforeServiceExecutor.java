package com.codingapi.tx.client.aspect.control;

import com.codingapi.tx.spi.sleuth.TracerHelper;
import com.codingapi.tx.commons.annotation.TxTransaction;
import com.codingapi.tx.client.bean.TxTransactionInfo;
import com.codingapi.tx.client.bean.TxTransactionLocal;
import com.codingapi.tx.client.framework.control.LCNTransactionServiceExecutor;
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

    private final LCNTransactionServiceExecutor transactionServiceExecutor;

    @Autowired
    public AspectBeforeServiceExecutor(TracerHelper tracerHelper,
                                       LCNTransactionServiceExecutor transactionServiceExecutor) {
        this.tracerHelper = tracerHelper;
        this.transactionServiceExecutor = transactionServiceExecutor;
    }

    public Object around(ProceedingJoinPoint point) throws Throwable {

        // build TransactionInfo 获取切面方法参数
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> clazz = point.getTarget().getClass();
        Object[] parameters = point.getArgs();
        Method thisMethod = clazz.getMethod(method.getName(), method.getParameterTypes());

        TransactionInfo transactionInfo = new TransactionInfo(clazz,
                thisMethod.getName(),
                thisMethod.toString(),
                parameters,
                method.getParameterTypes());

        // 事务发起方判断
        boolean isTransactionStart = tracerHelper.getGroupId() == null;
        TxTransaction transaction = thisMethod.getAnnotation(TxTransaction.class);
        assert Objects.nonNull(transaction);

        // 该线程事务
        String groupId = isTransactionStart ? RandomUtils.randomKey() : tracerHelper.getGroupId();
        String unitId = Transactions.unitId(transactionInfo.getMethodStr());
        TxTransactionLocal txTransactionLocal = TxTransactionLocal.getOrNew();
        if (txTransactionLocal.getUnitId() != null) {
            txTransactionLocal.setInUnit(true);
            log.info("tx > unit[{}] in unit: {}", unitId, txTransactionLocal.getUnitId());
        }
        txTransactionLocal.setUnitId(unitId);
        txTransactionLocal.setGroupId(groupId);
        txTransactionLocal.setTransactionType(transaction.type());

        // 事务参数
        TxTransactionInfo info = new TxTransactionInfo(
                transaction.type(),
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
            TxTransactionLocal.makeNeverAppeared();
        }
    }
}
