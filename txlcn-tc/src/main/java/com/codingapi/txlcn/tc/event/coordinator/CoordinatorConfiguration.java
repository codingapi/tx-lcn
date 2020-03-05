package com.codingapi.txlcn.tc.event.coordinator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Configuration
public class CoordinatorConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CoordinatorListener coordinatorListener(@Autowired(required = false) List<CoordinatorListener> listeners){
        return new TransactionCoordinatorListener(listeners);
    }

}
