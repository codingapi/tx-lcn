package com.codingapi.txlcn.tc.control.step;

import com.codingapi.txlcn.tc.config.TxConfig;
import com.codingapi.txlcn.tc.control.TransactionCommitorStrategy;
import com.codingapi.txlcn.tc.control.TransactionStep;
import com.codingapi.txlcn.tc.reporter.TxManagerReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lorne
 * @date 2020/3/5
 * @description
 */
@Configuration
public class RunnerConfiguration {

    @Bean
    public TransactionStep transactionStepCreate(TxManagerReporter managerProtocoler){
        return new TransactionStepCreate(managerProtocoler);
    }


    @Bean
    public TransactionStep transactionStepNotify(TxManagerReporter managerProtocoler,
                                                 TransactionCommitorStrategy transactionCommitorStrategy){
        return new TransactionStepNotify(managerProtocoler,transactionCommitorStrategy);
    }


    @Bean
    public TransactionStep transactionStepJoin(TxManagerReporter managerProtocoler,
                                               TransactionCommitorStrategy transactionCommitorStrategy,
                                               TxConfig txConfig){
        return new TransactionStepJoin(managerProtocoler,transactionCommitorStrategy,txConfig);
    }

}
