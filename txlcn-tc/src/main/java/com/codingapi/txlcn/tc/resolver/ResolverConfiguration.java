package com.codingapi.txlcn.tc.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 事务注解解析器配置
 * @author lorne  2020-03-05
 */
@Configuration
public class ResolverConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AnnotationContext annotationContext(@Autowired(required = false) List<AnnotationStrategy> annotationStrategies){
        return new AnnotationContext(annotationStrategies);
    }

    @Bean
    @ConditionalOnMissingBean
    public LcnAnnotationStrategy lcnAnnotationStrategy(){
        return new LcnAnnotationStrategy();
    }
}
