package com.codingapi.tx.client.bean;

import com.codingapi.tx.commons.annotation.DTXPropagation;
import com.codingapi.tx.commons.bean.TransactionInfo;
import com.codingapi.tx.commons.util.Transactions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * Description:
 * Date: 19-1-11 下午1:21
 *
 * @author ujued
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DTXInfo {

    private String transactionType;

    private DTXPropagation transactionPropagation;

    private TransactionInfo transactionInfo;

    private Method businessMethod;

    private String unitId;

    public DTXInfo(ProceedingJoinPoint point) throws Throwable {
        // build TransactionInfo 获取切面方法参数
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        Method thisMethod = point.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
        this.transactionInfo = new TransactionInfo();
        this.transactionInfo.setTargetClazz(point.getTarget().getClass());
        this.transactionInfo.setArgumentValues(point.getArgs());
        this.transactionInfo.setMethod(thisMethod.getName());
        this.transactionInfo.setMethodStr(thisMethod.toString());
        this.transactionInfo.setParameterTypes(method.getParameterTypes());


        this.businessMethod = thisMethod;
        this.unitId = Transactions.unitId(point.getSignature().toString());
    }

    public DTXInfo(MethodInvocation invocation) {
        Class<?>[] types = new Class[invocation.getArguments().length];
        for (int i = 0; i < invocation.getArguments().length; i++) {
            types[i] = invocation.getArguments()[i].getClass();
        }
        this.transactionInfo = new TransactionInfo();
        this.transactionInfo.setTargetClazz(invocation.getThis().getClass());
        this.transactionInfo.setArgumentValues(invocation.getArguments());
        this.transactionInfo.setMethod(invocation.getMethod().getName());
        this.transactionInfo.setMethodStr(invocation.getMethod().toString());
        this.transactionInfo.setParameterTypes(types);

        this.businessMethod = invocation.getMethod();
        this.unitId = Transactions.unitId(invocation.getMethod().toString());
    }

    public void reanalyseMethodArgs(Object[] args) {
        this.transactionInfo.setArgumentValues(args);
    }
}