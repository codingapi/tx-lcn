package com.codingapi.txlcn.tc.aspect;

import com.codingapi.txlcn.tc.control.TransactionStateControl;
import com.codingapi.txlcn.tc.parser.AnnotationParserHelper;
import com.codingapi.txlcn.tc.parser.TxAnnotation;
import com.codingapi.txlcn.tc.state.TransactionStateManager;
import com.codingapi.txlcn.tc.utils.PointUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@AllArgsConstructor
public class TransactionAspect implements Ordered {

  private TransactionStateControl transactionStateControl;

  private AnnotationParserHelper annotationParserHelper;

  @Pointcut("@annotation(com.codingapi.txlcn.tc.annotation.LcnTransaction)")
  public void lcnTransactionPointcut() {
  }

  @Around("lcnTransactionPointcut()")
  public Object runWithLcnTransaction(ProceedingJoinPoint point) throws Throwable {

    Method targetMethod = PointUtils.targetMethod(point);

    TxAnnotation txAnnotation = annotationParserHelper.getAnnotation(targetMethod);

    TransactionStateManager transactionStateManager = new TransactionStateManager(txAnnotation);
    if(!transactionStateManager.existTransactionState()){
        return point.proceed();
    }

    log.info("run with lcn start...");
    transactionStateControl.tryBeginTransaction(transactionStateManager.getTransactionState());
    Object res = point.proceed();
    transactionStateControl.tryEndTransaction(transactionStateManager.getTransactionState());
    log.info("run with lcn over");
    return res;
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
