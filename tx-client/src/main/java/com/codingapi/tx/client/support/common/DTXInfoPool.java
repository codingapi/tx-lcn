package com.codingapi.tx.client.support.common;

import com.codingapi.tx.client.bean.DTXInfo;
import com.codingapi.tx.commons.util.Transactions;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.Map;
import java.util.Objects;

/**
 * Description:
 * Date: 19-1-11 下午1:26
 *
 * @author ujued
 */
public class DTXInfoPool {
    private static final DTXInfoPool dtxInfoPool = new DTXInfoPool();
    private Map<String, DTXInfo> dtxInfoCache = new ConcurrentReferenceHashMap<>();

    private DTXInfoPool() {
    }

    private DTXInfo get0(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String signature = proceedingJoinPoint.getSignature().toString();
        String unitId = Transactions.unitId(signature);
        DTXInfo dtxInfo = dtxInfoCache.get(unitId);
        if (Objects.isNull(dtxInfo)) {
            dtxInfo = new DTXInfo(proceedingJoinPoint);
            dtxInfoCache.put(unitId, dtxInfo);
        }
        dtxInfo.reanalyseMethodArgs(proceedingJoinPoint.getArgs());
        return dtxInfo;
    }

    private DTXInfo get0(MethodInvocation methodInvocation) throws Throwable {
        String signature = methodInvocation.getMethod().toString();
        String unitId = Transactions.unitId(signature);
        DTXInfo dtxInfo = dtxInfoCache.get(unitId);
        if (Objects.isNull(dtxInfo)) {
            dtxInfo = new DTXInfo(methodInvocation);
            dtxInfoCache.put(unitId, dtxInfo);
        }
        dtxInfo.reanalyseMethodArgs(methodInvocation.getArguments());
        return dtxInfo;
    }

    public static DTXInfo get(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return dtxInfoPool.get0(proceedingJoinPoint);
    }

    public static DTXInfo get(MethodInvocation methodInvocation) throws Throwable {
        return dtxInfoPool.get0(methodInvocation);
    }
}
