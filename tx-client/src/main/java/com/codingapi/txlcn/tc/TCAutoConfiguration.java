package com.codingapi.txlcn.tc;

import com.codingapi.txlcn.commons.runner.TxLcnApplicationRunner;
import com.codingapi.txlcn.logger.TxLoggerConfiguration;
import com.codingapi.txlcn.spi.sleuth.TxSleuthApiConfiguration;
import com.codingapi.txlcn.tc.config.DependenciesImportSelector;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Description:
 * Date: 1/19/19
 *
 * @author ujued
 * @see EnableDistributedTransaction
 */
@Configuration
@ComponentScan
@Import({TxLoggerConfiguration.class, TxSleuthApiConfiguration.class, DependenciesImportSelector.class})
public class TCAutoConfiguration {

    /**
     * All initialization about TX-LCN
     *
     * @param applicationContext Spring ApplicationContext
     * @return TX-LCN custom runner
     */
    @Bean
    public ApplicationRunner txLcnApplicationRunner(ApplicationContext applicationContext) {
        return new TxLcnApplicationRunner(applicationContext);
    }
}
