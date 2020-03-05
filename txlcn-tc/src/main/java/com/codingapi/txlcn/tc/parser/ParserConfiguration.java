package com.codingapi.txlcn.tc.parser;

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
public class ParserConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AnnotationParserHelper annotationParserHelper(@Autowired(required = false) List<AnnotationParser> annotationParsers){
        return new AnnotationParserHelper(annotationParsers);
    }

    @Bean
    @ConditionalOnMissingBean
    public LcnAnnotationParser lcnAnnotationParser(){
        return new LcnAnnotationParser();
    }
}
