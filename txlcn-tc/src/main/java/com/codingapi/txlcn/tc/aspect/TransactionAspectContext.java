package com.codingapi.txlcn.tc.aspect;

import com.codingapi.maven.uml.annotation.GraphRelation;
import com.codingapi.maven.uml.annotation.Model;
import com.codingapi.txlcn.tc.control.TransactionContext;
import com.codingapi.txlcn.tc.control.TransactionState;
import com.codingapi.txlcn.tc.control.TransactionStateStrategy;
import com.codingapi.txlcn.tc.info.TransactionInfo;
import com.codingapi.txlcn.tc.resolver.AnnotationContext;
import com.codingapi.txlcn.tc.resolver.TxAnnotation;
import com.codingapi.txlcn.tc.utils.PointUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

@Slf4j
@AllArgsConstructor
@Model(flag = "C",value = "注解执行时",color = "#FF88EE")
public class TransactionAspectContext {

  @GraphRelation(value = "-->",type = TransactionContext.class)
  private TransactionContext transactionContext;

  @GraphRelation(value = "-->",type = TransactionContext.class)
  private AnnotationContext annotationContext;

  public Object runWithTransaction(ProceedingJoinPoint point) throws Throwable {

    Method targetMethod = PointUtils.targetMethod(point);

    TxAnnotation txAnnotation = annotationContext.getAnnotation(targetMethod);
    if(txAnnotation==null){
      return point.proceed();
    }

    TransactionState transactionState = TransactionStateStrategy.getTransactionState();
    TransactionInfo transactionInfo = new TransactionInfo(txAnnotation.getType(),transactionState);

    log.debug("run with tx-lcn start...");
    Object res = null;
    try {
      transactionContext.tryBeginTransaction(transactionInfo);
      res = point.proceed();
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
