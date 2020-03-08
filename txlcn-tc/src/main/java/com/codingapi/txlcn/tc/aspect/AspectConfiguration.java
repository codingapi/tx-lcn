package com.codingapi.txlcn.tc.aspect;

import com.codingapi.txlcn.tc.control.TransactionStateControl;
import com.codingapi.txlcn.tc.parser.AnnotationParserHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AspectConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public TransactionAspectManager transactionAspectManager(TransactionStateControl transactionStateControl,
                                                    AnnotationParserHelper annotationParserHelper) {
    return new TransactionAspectManager(transactionStateControl,annotationParserHelper);
  }

  @Bean
  @ConditionalOnMissingBean
  public LcnTransactionAspect lcnTransactionAspect(TransactionAspectManager transactionAspectManager){
    return new LcnTransactionAspect(transactionAspectManager);
  }

}
