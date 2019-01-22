package com.codingapi.txlcn.client.config;

import com.codingapi.txlcn.client.TCConfiguration;
import com.codingapi.txlcn.logger.TxLoggerConfiguration;
import com.codingapi.txlcn.spi.sleuth.TxSleuthApiConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Description: 允许分布式事务的注解
 * Date: 1/19/19
 *
 * @author ujued
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({TCConfiguration.class,
        TxLoggerConfiguration.class,
        TxSleuthApiConfiguration.class,
        DependenciesImportSelector.class})
public @interface EnableDistributedTransaction {

}
