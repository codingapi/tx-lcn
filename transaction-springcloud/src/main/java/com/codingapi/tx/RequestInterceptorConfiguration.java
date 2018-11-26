package com.codingapi.tx;

import com.codingapi.tx.springcloud.feign.TransactionRestTemplateInterceptor;
import com.codingapi.tx.springcloud.http.TransactionHttpRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * create by lorne on 2018/1/18
 */
@Configuration
public class RequestInterceptorConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new TransactionRestTemplateInterceptor();
    }

    @Bean
    public TransactionHttpRequestInterceptor transactionHttpRequestInterceptor(@Autowired(required = false) RestTemplate restTemplate){
        TransactionHttpRequestInterceptor transactionHttpRequestInterceptor = new TransactionHttpRequestInterceptor();
        if(restTemplate != null){
            restTemplate.getInterceptors().add(transactionHttpRequestInterceptor);
        }
        return transactionHttpRequestInterceptor;
    }
}
