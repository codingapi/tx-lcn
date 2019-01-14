package com.codingapi.txlcn.manager.support.restapi.auth.sauth;

import com.codingapi.txlcn.manager.support.restapi.auth.sauth.token.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Description:
 * Date: 2018/11/23
 *
 * @auther ujued
 */
@Configuration
@ComponentScan
public class SAuthConfiguration {

    @Bean
    public TokenInterceptor tokenInterceptor(SAuthLogic sAuthLogic) {
        return new TokenInterceptor(sAuthLogic);
    }
}
