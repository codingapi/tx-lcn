package com.codingapi.txlcn.tc.aspect;

import com.codingapi.txlcn.tc.control.TransactionStateControl;
import com.codingapi.txlcn.tc.info.TransactionInfoManager;
import com.codingapi.txlcn.tc.parser.AnnotationParserHelper;
import com.codingapi.txlcn.tc.parser.TxAnnotation;
import com.codingapi.txlcn.tc.utils.PointUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

@Slf4j
@AllArgsConstructor
public class TransactionAspectManager {

  private TransactionStateControl transactionStateControl;

  private AnnotationParserHelper annotationParserHelper;

  public Object runWithTransaction(ProceedingJoinPoint point) throws Throwable {

    Method targetMethod = PointUtils.targetMethod(point);

    TxAnnotation txAnnotation = annotationParserHelper.getAnnotation(targetMethod);

    TransactionInfoManager transactionInfoManager = new TransactionInfoManager(txAnnotation);
    if(!transactionInfoManager.existTransaction()){
        return point.proceed();
    }

    log.info("run with lcn start...");
    transactionStateControl.tryBeginTransaction(transactionInfoManager.getTransactionInfo());
    Object res = point.proceed();
    transactionStateControl.tryEndTransaction(transactionInfoManager.getTransactionInfo());
    log.info("run with lcn over");
    return res;
  }

}
