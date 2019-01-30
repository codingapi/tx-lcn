package com.codingapi.txlcn.tracing.http;

import com.codingapi.txlcn.common.util.Maps;
import com.codingapi.txlcn.tracing.TracingConstants;
import com.codingapi.txlcn.tracing.TracingContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Description:
 * Date: 19-1-28 下午4:59
 *
 * @author ujued
 */
@ConditionalOnClass(HandlerInterceptor.class)
@Component
public class TracingHandlerInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        String groupId = Optional.ofNullable(request.getHeader(TracingConstants.HEADER_KEY_GROUP_ID)).orElse("");
        String appList = Optional.ofNullable(request.getHeader(TracingConstants.HEADER_KEY_APP_MAP)).orElse("");
        TracingContext.tracing().init(Maps.newHashMap(TracingConstants.GROUP_ID, groupId, TracingConstants.APP_MAP, appList));
        return true;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }
}
