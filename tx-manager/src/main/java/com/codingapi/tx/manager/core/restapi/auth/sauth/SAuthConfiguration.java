package com.codingapi.tx.manager.core.restapi.auth.sauth;

import com.codingapi.tx.manager.core.restapi.auth.sauth.token.TokenInterceptor;
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
