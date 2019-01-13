package com.codingapi.tx.client.aspect.interceptor;

import com.codingapi.tx.client.aspect.weave.DTXLogicWeaver;
import com.codingapi.tx.client.bean.DTXInfo;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * Description:
 * Date: 1/12/19
 *
 * @author ujued
 */

public class DTXInterceptor extends TransactionInterceptor {

    private final DTXLogicWeaver dtxLogicWeaver;

    public DTXInterceptor(DTXLogicWeaver dtxLogicWeaver) {
        this.dtxLogicWeaver = dtxLogicWeaver;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        DTXInfo dtxInfo = InterceptorInvocationUtils.load(invocation);
        return dtxLogicWeaver.runTransaction(dtxInfo, () -> super.invoke(invocation));
    }

}
