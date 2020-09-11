package com.codingapi.txlcn.tm.loadbalancer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WhomHim
 * @description
 * @date Create in 2020/9/9 17:57
 */
@Configuration
public class InterceptorConfiguration {

    @Bean
    public EventInterceptor interceptor() {
        return new LoadBalancerInterceptor();
    }
}
