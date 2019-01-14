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
package com.codingapi.txlcn.client.support.common;

import com.codingapi.txlcn.client.bean.DTXInfo;
import com.codingapi.txlcn.commons.util.Transactions;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.lang.reflect.Method;
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
            MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
            Method method = methodSignature.getMethod();
            Class<?> targetClass = proceedingJoinPoint.getTarget().getClass();
            Method thisMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
            dtxInfo = new DTXInfo(thisMethod, proceedingJoinPoint.getArgs(), targetClass);
            dtxInfoCache.put(unitId, dtxInfo);
        }
        dtxInfo.reanalyseMethodArgs(proceedingJoinPoint.getArgs());
        return dtxInfo;
    }

    private DTXInfo get0(MethodInvocation methodInvocation) {
        String signature = methodInvocation.getMethod().toString();
        String unitId = Transactions.unitId(signature);
        DTXInfo dtxInfo = dtxInfoCache.get(unitId);
        if (Objects.isNull(dtxInfo)) {
            dtxInfo = new DTXInfo(methodInvocation.getMethod(),
                    methodInvocation.getArguments(), methodInvocation.getThis().getClass());
            dtxInfoCache.put(unitId, dtxInfo);
        }
        dtxInfo.reanalyseMethodArgs(methodInvocation.getArguments());
        return dtxInfo;
    }

    private DTXInfo get0(Method method, Object[] args, Class<?> targetClass) {
        String signature = method.getName();
        String unitId = Transactions.unitId(signature);
        DTXInfo dtxInfo = dtxInfoCache.get(unitId);
        if (Objects.isNull(dtxInfo)) {
            dtxInfo = new DTXInfo(method, args, targetClass);
            dtxInfoCache.put(unitId, dtxInfo);
        }
        dtxInfo.reanalyseMethodArgs(args);
        return dtxInfo;
    }

    public static DTXInfo get(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return dtxInfoPool.get0(proceedingJoinPoint);
    }

    public static DTXInfo get(MethodInvocation methodInvocation) {
        return dtxInfoPool.get0(methodInvocation);
    }

    public static DTXInfo get(Method method, Object[] args, Class<?> targetClass) {
        return dtxInfoPool.get0(method, args, targetClass);
    }
}
