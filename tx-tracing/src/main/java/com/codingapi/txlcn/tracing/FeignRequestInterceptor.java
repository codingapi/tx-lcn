package com.codingapi.txlcn.tracing;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 19-1-28 下午3:47
 *
 * @author ujued
 */
@ConditionalOnClass(Feign.class)
@Component
@Order
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(TracingConstants.HEADER_KEY_GROUP_ID, TracingContext.tracing().groupId());
        requestTemplate.header(TracingConstants.HEADER_KEY_APP_LIST, TracingContext.tracing().appListString());
    }
}
