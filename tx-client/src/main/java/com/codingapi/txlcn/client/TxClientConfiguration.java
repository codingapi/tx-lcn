package com.codingapi.txlcn.client;

import com.codingapi.txlcn.client.aspect.DataSourceAspect;
import com.codingapi.txlcn.client.aspect.TransactionAspect;
import com.codingapi.txlcn.client.aspect.weave.DTXLogicWeaver;
import com.codingapi.txlcn.client.aspect.weave.DTXResourceWeaver;
import com.codingapi.txlcn.client.config.EnableDistributedTransaction;
import com.codingapi.txlcn.client.config.TxClientConfig;
import com.codingapi.txlcn.client.support.TXLCNTransactionBeanHelper;
import com.codingapi.txlcn.client.support.TXLCNTransactionServiceExecutor;
import com.codingapi.txlcn.client.support.cache.TransactionAttachmentCache;
import com.codingapi.txlcn.commons.runner.TxLcnApplicationRunner;
import com.codingapi.txlcn.spi.sleuth.TracerHelper;
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
public class TxClientConfiguration {

    @Bean
    public ApplicationRunner txLcnApplicationRunner(ApplicationContext applicationContext) {
        return new TxLcnApplicationRunner(applicationContext);
    }

    @Bean
    public DataSourceAspect dataSourceAspect(TxClientConfig clientConfig, DTXResourceWeaver resourceWeaver) {
        return new DataSourceAspect(clientConfig, resourceWeaver);
    }

    @Bean
    public TransactionAspect transactionAspect(TxClientConfig clientConfig, DTXLogicWeaver logicWeaver) {
        return new TransactionAspect(clientConfig, logicWeaver);
    }

    @Bean
    public DTXLogicWeaver dtxLogicWeaver(TracerHelper tracerHelper,
                                         TXLCNTransactionServiceExecutor transactionServiceExecutor,
                                         TransactionAttachmentCache transactionAttachmentCache) {
        return new DTXLogicWeaver(tracerHelper, transactionServiceExecutor, transactionAttachmentCache);
    }

    @Bean
    public DTXResourceWeaver dtxResourceWeaver(TXLCNTransactionBeanHelper transactionBeanHelper) {
        return new DTXResourceWeaver(transactionBeanHelper);
    }
}
