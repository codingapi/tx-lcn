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
package com.codingapi.txlcn.tm.support.restapi.auth.sauth.token;

import com.codingapi.txlcn.tm.support.restapi.auth.sauth.SAuthHandleException;
import com.codingapi.txlcn.tm.support.restapi.auth.sauth.SAuthLogic;
import com.codingapi.txlcn.tm.support.restapi.vo.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Description:
 * Date: 2018/11/23
 *
 * @author ujued
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(TokenInterceptor.class);

    private final SAuthLogic sAuthLogic;

    @Autowired
    public TokenInterceptor(SAuthLogic sAuthLogic) {
        this.sAuthLogic = sAuthLogic;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 提供认证逻辑忽略地址
        for (String url : sAuthLogic.ignoreUrls()) {
            int ind = url.indexOf("*");
            if (ind != -1 && request.getRequestURI().startsWith(url.substring(0, ind))) {
                return true;
            }
            if (request.getRequestURI().equalsIgnoreCase(url)) {
                return true;
            }
        }
        try {
            if (sAuthLogic.isIgnored(request)) {
                LOG.info("Ignored caused logic.");
                return true;
            }
        } catch (SAuthHandleException e) {
            responseError(HttpStatus.FORBIDDEN.value(), e.getMessage(), response);
            return false;
        }

        String token = request.getHeader("Authorization");
        if ((Objects.isNull(token))) {
            token = request.getParameter("token");
        }
        if (StringUtils.isEmpty(token)) {
            LOG.warn("Unauthorized, token is null. URL: " + request.getRequestURI());
            responseError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized.", response);
            return false;
        }
        LOG.info("Token is: {}", token);
        if (!sAuthLogic.verify(token)) {
            LOG.warn("Unauthorized, token is invalid.");
            responseError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized, token is invalid.", response);
            return false;
        }
        return true;
    }

    private void responseError(int code, String message, HttpServletResponse response) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(code);
        errorResponse.setMessage(message);
        response.setStatus(code);
        response.setCharacterEncoding("utf8");
        try {
            response.getOutputStream().write(errorResponse.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
