package com.codingapi.txlcn.tc.aspect;

import com.codingapi.maven.uml.annotation.GraphRelation;
import com.codingapi.maven.uml.annotation.Model;
import com.codingapi.txlcn.tc.control.TransactionContext;
import com.codingapi.txlcn.tc.control.TransactionState;
import com.codingapi.txlcn.tc.control.TransactionStateStrategy;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.resolver.AnnotationContext;
import com.codingapi.txlcn.tc.resolver.TxAnnotation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

@Slf4j
@AllArgsConstructor
@Model(flag = "C",value = "注解执行时",color = "#FF88EE")
public class TransactionAspectContext {

  @GraphRelation(value = "-->",type = TransactionContext.class)
  private TransactionContext transactionContext;

  @GraphRelation(value = "-->",type = TransactionContext.class)
  private AnnotationContext annotationContext;

  public Object runWithTransaction(MethodInvocation invocation) throws Throwable {
    Method targetMethod = invocation.getMethod();

    TxAnnotation txAnnotation = annotationContext.getAnnotation(targetMethod);
    if(txAnnotation==null){
      return invocation.proceed();
    }

    TransactionState transactionState = TransactionStateStrategy.getTransactionState();
    TransactionInfo transactionInfo = new TransactionInfo(txAnnotation.getType(),transactionState);

    log.debug("run with tx-lcn start...");
    Object res = null;
    try {
      transactionContext.tryBeginTransaction(transactionInfo);
      res = invocation.proceed();
      transactionInfo.setSuccessReturn(true);
    }catch (Exception e){
      transactionInfo.setSuccessReturn(false);
      throw e;
    }finally {
      transactionContext.tryEndTransaction(transactionInfo);
    }
    log.debug("run with tx-lcn over.");
    return res;
  }

}
