package com.codingapi.txlcn.tc;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.codingapi.txlcn.commons.runner.TxLcnApplicationRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Date: 1/19/19
 *
 * @author ujued
 * @see EnableDistributedTransaction
 */
@Configuration
@ComponentScan
public class TCConfiguration {

    @Bean
    public ApplicationRunner txLcnApplicationRunner(ApplicationContext applicationContext) {
        return new TxLcnApplicationRunner(applicationContext);
    }
}
