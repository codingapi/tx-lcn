package com.codingapi.example.client;

import com.codingapi.tx.client.aspect.interceptor.DTXInterceptor;
import com.codingapi.tx.client.aspect.weave.DTXLogicWeaver;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.autoconfigure.transaction.TransactionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Properties;

/**
 * Description:
 * Date: 19-1-13 下午2:46
 *
 * @author ujued
 */
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(TransactionProperties.class)
public class TransactionConfiguration {

    @Bean
    public TransactionInterceptor transactionInterceptor(PlatformTransactionManager transactionManager, DTXLogicWeaver dtxLogicWeaver) {
        Properties properties = new Properties();
        properties.setProperty("*", "PROPAGATION_REQUIRED,-Throwable");

        DTXInterceptor dtxInterceptor = new DTXInterceptor(dtxLogicWeaver);
        dtxInterceptor.setTransactionManager(transactionManager);
        dtxInterceptor.setTransactionAttributes(properties);
        return dtxInterceptor;
    }

    @Bean
    public BeanNameAutoProxyCreator beanNameAutoProxyCreator() {
        BeanNameAutoProxyCreator beanNameAutoProxyCreator = new BeanNameAutoProxyCreator();
        beanNameAutoProxyCreator.setInterceptorNames("transactionInterceptor");
        beanNameAutoProxyCreator.setBeanNames("*Impl");
        return beanNameAutoProxyCreator;
    }
}
