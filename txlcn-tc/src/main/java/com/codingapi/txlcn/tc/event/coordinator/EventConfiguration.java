package com.codingapi.txlcn.tc.event.coordinator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class EventConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public TransactionCoordinatorListener transactionCoordinatorListener(
     @Autowired(required = false) List<CoordinatorListener> coordinatorListeners) {
    return new TransactionCoordinatorListener(coordinatorListeners);
  }

}
