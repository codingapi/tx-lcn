package com.codingapi.txlcn.tc.aspect;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

@Slf4j
@Aspect
public class TransactionAspect implements Ordered {

  @Pointcut("@annotation(com.codingapi.txlcn.tc.annotation.LcnTransaction)")
  public void lcnTransactionPointcut() {
  }

  @Around("lcnTransactionPointcut()")
  public Object runWithLcnTransaction(ProceedingJoinPoint point) throws Throwable {
    log.info("run with lcn start...");
    MethodSignature methodSignature = (MethodSignature) point.getSignature();
    Method method = methodSignature.getMethod();
    Class<?> targetClass = point.getTarget().getClass();
    Method thisMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
    LcnTransaction lcnTransaction = thisMethod.getAnnotation(LcnTransaction.class);
    log.info("run with lcn :{}", lcnTransaction);
    Object res = point.proceed();
    log.info("run with lcn over");
    return res;
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
