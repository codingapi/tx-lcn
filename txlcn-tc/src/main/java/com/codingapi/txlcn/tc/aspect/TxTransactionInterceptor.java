package com.codingapi.txlcn.tc.aspect;


import com.codingapi.maven.uml.annotation.Model;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author lorne
 * @date 2020/7/1
 * @description
 */
@Model(flag = "C",value = "LCN注解切面",color = "#FF88EE")
public class TxTransactionInterceptor implements MethodInterceptor {

    private TransactionAspectContext transactionAspectContext;

    public TxTransactionInterceptor(TransactionAspectContext transactionAspectContext) {
        this.transactionAspectContext = transactionAspectContext;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return transactionAspectContext.runWithTransaction(invocation);
    }
}
