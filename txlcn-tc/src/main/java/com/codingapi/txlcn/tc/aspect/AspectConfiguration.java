package com.codingapi.txlcn.tc.aspect;

import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.control.TransactionContext;
import com.codingapi.txlcn.tc.resolver.AnnotationContext;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AspectConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public TransactionAspectContext transactionAspectManager(TransactionContext transactionContext,
                                                           AnnotationContext annotationContext) {
    return new TransactionAspectContext(transactionContext, annotationContext);
  }

  @Bean
  @ConditionalOnMissingBean
  public Advisor txTransactionAdvisor(TxTransactionInterceptor txTransactionInterceptor, TxConfig txConfig){
    AspectJExpressionPointcut pointcut=new AspectJExpressionPointcut();
    pointcut.setExpression(txConfig.getPointcut());
    return new DefaultPointcutAdvisor(pointcut, txTransactionInterceptor);
  }

  @Bean(name = "txTransactionInterceptor")
  @ConditionalOnMissingBean
  public TxTransactionInterceptor txTransactionInterceptor(TransactionAspectContext transactionAspectContext){
    return new TxTransactionInterceptor(transactionAspectContext);
  }


}
