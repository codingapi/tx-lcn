package com.codingapi.txlcn.tc.control;

import com.codingapi.txlcn.tc.event.coordinator.CoordinatorListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Configuration
public class ControlConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public TransactionStateControl transactionStateControl(CoordinatorListener coordinatorListener){
        return new TransactionStateControl(coordinatorListener);
    }
}
