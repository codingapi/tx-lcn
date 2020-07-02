package com.codingapi.txlcn.tc.aspect;


import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@AllArgsConstructor
public class TxTransactionInterceptor implements MethodInterceptor {

    private TransactionAspectContext transactionAspectContext;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return transactionAspectContext.runWithTransaction(invocation);
    }
}
