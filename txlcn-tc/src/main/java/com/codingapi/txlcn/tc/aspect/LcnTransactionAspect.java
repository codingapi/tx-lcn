package com.codingapi.txlcn.tc.aspect;

import com.codingapi.maven.uml.annotation.GraphRelation;
import com.codingapi.maven.uml.annotation.Model;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

@Slf4j
@AllArgsConstructor
@Aspect
@Model(flag = "C",value = "LCN注解切面",color = "#FF88EE")
public class LcnTransactionAspect implements Ordered {

  @GraphRelation(value = "-->",type = TransactionAspectContext.class)
  private TransactionAspectContext transactionAspectContext;

  @Pointcut("@annotation(com.codingapi.txlcn.tc.annotation.LcnTransaction)")
  public void lcnTransactionPointcut() {

  }

  @Around("lcnTransactionPointcut()")
  public Object runWithLcnTransaction(ProceedingJoinPoint point) throws Throwable{
    return transactionAspectContext.runWithTransaction(point);
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
