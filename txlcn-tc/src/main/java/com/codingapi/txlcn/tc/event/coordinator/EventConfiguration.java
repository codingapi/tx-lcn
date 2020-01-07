package com.codingapi.txlcn.tc.event.coordinator;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EventConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public TransactionCoordinatorListener transactionCoordinatorListener(List<CoordinatorListener> coordinatorListeners){
    return new TransactionCoordinatorListener(coordinatorListeners);
  }

  @Bean("defaultCoordinatorListener")
  public CoordinatorListener defaultCoordinatorListener(){
    return new DefaultCoordinatorListener();
  }


}
