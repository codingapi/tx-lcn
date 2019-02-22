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
package com.codingapi.txlcn.tm.support.restapi.auth.sauth;


import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * Description:
 * Date: 2018/11/23
 *
 * @author ujued
 */
public interface SAuthLogic {
    
    /**
     * ignore path
     *
     * @return list
     */
    default List<String> ignoreUrls() {
        return Collections.emptyList();
    }
    
    /**
     * is ignore
     *
     * @param request request
     * @return bool
     * @throws SAuthHandleException SAuthHandleException
     */
    default boolean isIgnored(HttpServletRequest request) throws SAuthHandleException {
        return false;
    }
    
    /**
     * verify token
     *
     * @param token token
     * @return bool
     */
    boolean verify(String token);
}
