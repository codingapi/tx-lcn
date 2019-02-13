/*
 * Copyright 2017-2019 CodingApi .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codingapi.txlcn.tracing.http;

import com.codingapi.txlcn.tracing.Tracings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Description:
 * Date: 19-1-28 下午4:35
 *
 * @author ujued
 */
@ConditionalOnClass(RestTemplate.class)
@Component
@Order
public class RestTemplateTracingTransmitter implements ClientHttpRequestInterceptor {

    @Autowired
    public RestTemplateTracingTransmitter(@Autowired(required = false) List<RestTemplate> restTemplates) {
        if (Objects.nonNull(restTemplates)) {
            restTemplates.forEach(restTemplate -> {
                List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
                interceptors.add(interceptors.size(), RestTemplateTracingTransmitter.this);
            });
        }
    }

    @Override
    @NonNull
    public ClientHttpResponse intercept(
            @NonNull HttpRequest httpRequest, @NonNull byte[] bytes,
            @NonNull ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        Tracings.transmit(httpRequest.getHeaders()::add);
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
