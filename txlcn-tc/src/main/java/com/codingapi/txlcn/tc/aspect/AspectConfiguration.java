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
  public TransactionAspect transactionAspect(TransactionStateControl transactionStateControl, AnnotationParserHelper annotationParserHelper) {
    return new TransactionAspect(transactionStateControl,annotationParserHelper);
  }

}
