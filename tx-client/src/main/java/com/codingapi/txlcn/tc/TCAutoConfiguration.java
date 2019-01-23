package com.codingapi.txlcn.tc;

import com.codingapi.txlcn.commons.runner.TxLcnApplicationRunner;
import com.codingapi.txlcn.logger.TxLoggerConfiguration;
import com.codingapi.txlcn.spi.sleuth.TxSleuthApiConfiguration;
import com.codingapi.txlcn.tc.config.DependenciesImportSelector;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

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

    /**
     * Spring ApplicationContext at least one Primary java.sql.DataSource.
     *
     * @param dataSource java.sql.DataSource
     * @return apache jdbc runner
     */
    @Bean
    @Primary
    public QueryRunner queryRunner(DataSource dataSource) {
        return new QueryRunner(dataSource);
    }
}
