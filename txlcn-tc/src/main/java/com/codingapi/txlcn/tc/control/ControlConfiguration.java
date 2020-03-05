package com.codingapi.txlcn.tc.control;

import com.codingapi.txlcn.tc.event.coordinator.TransactionCoordinatorListener;
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
public class ControlConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public TransactionStateControl transactionStateControl(TransactionCoordinatorListener transactionCoordinatorListener,TransactionStepExecuter transactionStepExecuter){
        return new TransactionStateControl(transactionCoordinatorListener,transactionStepExecuter);
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionStepExecuter transactionStepExecuter( @Autowired(required = false) List<TransactionStepRunner> transactionStepRunners){
        return new TransactionStepExecuter(transactionStepRunners);
    }


}
