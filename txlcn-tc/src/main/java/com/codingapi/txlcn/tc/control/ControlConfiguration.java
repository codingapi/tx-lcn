package com.codingapi.txlcn.tc.control;

import com.codingapi.txlcn.tc.event.transaction.TransactionEventContext;
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
    public TransactionContext transactionStateControl(TransactionEventContext transactionCoordinatorListener, TransactionStepContext transactionStepContext){
        return new TransactionContext(transactionCoordinatorListener, transactionStepContext);
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionStepContext transactionStepExecuter(@Autowired(required = false) List<TransactionStep> transactionSteps){
        return new TransactionStepContext(transactionSteps);
    }

    @Bean
    public TransactionCommitorStrategy transactionCommitor(@Autowired(required = false) List<Commitor> commitors){
        return new TransactionCommitorStrategy(commitors);
    }


}
