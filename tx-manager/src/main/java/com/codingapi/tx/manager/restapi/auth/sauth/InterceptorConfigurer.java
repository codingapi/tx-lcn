package com.codingapi.tx.manager.restapi.auth.sauth;

import com.codingapi.tx.manager.restapi.auth.sauth.token.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description:
 * Date: 2018/11/23
 *
 * @auther ujued
 */
@Component
public class InterceptorConfigurer implements WebMvcConfigurer {

    private final TokenInterceptor tokenInterceptor;

    @Autowired
    public InterceptorConfigurer(TokenInterceptor tokenInterceptor) {
        this.tokenInterceptor = tokenInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor);
    }
}
