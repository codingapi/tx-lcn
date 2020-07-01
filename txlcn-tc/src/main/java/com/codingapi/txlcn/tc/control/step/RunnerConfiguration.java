package com.codingapi.txlcn.tc.control.step;

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
    public TransactionStep transactionStepNotify(TxManagerReporter managerProtocoler){
        return new TransactionStepNotify(managerProtocoler);
    }

}
