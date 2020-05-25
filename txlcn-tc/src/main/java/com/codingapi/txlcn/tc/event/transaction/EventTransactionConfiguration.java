package com.codingapi.txlcn.tc.event.transaction;

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
public class EventTransactionConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TransactionEventContext coordinatorListener(@Autowired(required = false) List<TransactionEventListener> transactionEventListeners){
        return new TransactionEventContext(transactionEventListeners);
    }

    @Bean
    public TransactionEventListener defaultTransactionEventListener(){
        return new DefaultTransactionEventListener();
    }
}
