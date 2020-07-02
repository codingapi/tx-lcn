package com.codingapi.txlcn.tc.rpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author lorne
 * @date 2020/7/2
 * @description
 */
public class RestTemplateRpcTransactionInterceptor implements ClientHttpRequestInterceptor {


    @Autowired
    public RestTemplateRpcTransactionInterceptor(List<RestTemplate> restTemplates) {
        if (Objects.nonNull(restTemplates)) {
            restTemplates.forEach(restTemplate -> {
                List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
                interceptors.add(interceptors.size(), RestTemplateRpcTransactionInterceptor.this);
            });
        }
    }

    @Override
    @NonNull
    public ClientHttpResponse intercept(
            @NonNull HttpRequest httpRequest, @NonNull byte[] bytes,
            @NonNull ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        RpcTransactionContext.getInstance().build(httpRequest.getHeaders()::add);
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
