package com.codingapi.txlcn.tc.aspect;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AspectConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public TransactionAspect transactionAspect() {
    return new TransactionAspect();
  }

}
