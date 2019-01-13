package com.codingapi.tx.client.aspect.interceptor;

import com.codingapi.tx.client.aspect.weave.DTXLogicWeaver;
import com.codingapi.tx.client.bean.DTXInfo;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Description:
 * Date: 1/12/19
 *
 * @author ujued
 */
public class TXLCNInterceptor implements MethodInterceptor {

    private final DTXLogicWeaver dtxLogicWeaver;

    public TXLCNInterceptor(DTXLogicWeaver dtxLogicWeaver) {
        this.dtxLogicWeaver = dtxLogicWeaver;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        DTXInfo dtxInfo = InterceptorInvocationUtils.load(invocation);
        return dtxLogicWeaver.runTransaction(dtxInfo, invocation::proceed);
    }
}
