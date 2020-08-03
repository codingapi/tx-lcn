package com.codingapi.txlcn.tc.aspect;

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
public class TransactionAspectContext {

  private TransactionContext transactionContext;

  private AnnotationContext annotationContext;

  public Object runWithTransaction(MethodInvocation invocation) throws Throwable {
    Method targetMethod = invocation.getMethod();

    TxAnnotation txAnnotation = annotationContext.getAnnotation(targetMethod);
    if(txAnnotation==null){
      return invocation.proceed();
    }

    TransactionState transactionState = TransactionStateStrategy.getTransactionState();
    TransactionInfo transactionInfo = TransactionInfo.current();
    //当transactionInfo == null表明是开始分布式事务
    if(transactionInfo==null){
      transactionInfo = new TransactionInfo(transactionState);
    }
    //统一设置事务类型
    transactionInfo.setTransactionType(txAnnotation.getType());

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
      transactionContext.clearTransaction();
    }
    log.debug("run with tx-lcn over.");
    return res;
  }

}
