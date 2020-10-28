package com.codingapi.txlcn.tc.rpc;

import feign.Feign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

/**
 * @author lorne
 * @date 2020/7/2
 * @description
 */
@Configuration
public class RpcTransactionConfiguration {


    @ConditionalOnClass(RestTemplate.class)
    class RestTemplateConfiguration{

        @Bean
        public RestTemplateRpcTransactionInterceptor restTemplateRpcTransactionInterceptor(@Autowired(required = false) List<RestTemplate> restTemplates){
            return new RestTemplateRpcTransactionInterceptor(restTemplates);
        }

    }


    @ConditionalOnClass(Feign.class)
    class FeignConfiguration{

        @Bean
        public FeignRpcTransactionInterceptor feignRpcTransactionInterceptor(){
            return new FeignRpcTransactionInterceptor();
        }
    }


    @ConditionalOnClass(HandlerInterceptor.class)
    class WebHandlerInterceptorConfiguration{

        @Bean
        public RpcTransactionHandlerInterceptor rpcTransactionHandlerInterceptor(){
            return new RpcTransactionHandlerInterceptor();
        }

    }
}
