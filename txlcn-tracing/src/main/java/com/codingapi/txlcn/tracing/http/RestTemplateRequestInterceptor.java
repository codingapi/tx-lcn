package com.codingapi.txlcn.tracing.http;

import com.codingapi.txlcn.tracing.TracingConstants;
import com.codingapi.txlcn.tracing.TracingContext;
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

/**
 * Description:
 * Date: 19-1-28 下午4:35
 *
 * @author ujued
 */
@ConditionalOnClass(RestTemplate.class)
@Component
@Order
public class RestTemplateRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    @NonNull
    public ClientHttpResponse intercept(
            @NonNull HttpRequest httpRequest, @NonNull byte[] bytes,
            @NonNull ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        httpRequest.getHeaders().add(TracingConstants.HEADER_KEY_GROUP_ID, TracingContext.tracing().groupId());
        httpRequest.getHeaders().add(TracingConstants.HEADER_KEY_APP_MAP, TracingContext.tracing().appMapString());
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
