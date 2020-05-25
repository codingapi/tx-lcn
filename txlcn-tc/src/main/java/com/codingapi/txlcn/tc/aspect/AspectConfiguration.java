package com.codingapi.txlcn.tc.aspect;

import com.codingapi.txlcn.tc.control.TransactionContext;
import com.codingapi.txlcn.tc.resolver.AnnotationContext;
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
  public LcnTransactionAspect lcnTransactionAspect(TransactionAspectContext transactionAspectContext){
    return new LcnTransactionAspect(transactionAspectContext);
  }

}
