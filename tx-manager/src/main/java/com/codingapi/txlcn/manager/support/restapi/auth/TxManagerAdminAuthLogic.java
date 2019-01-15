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
package com.codingapi.txlcn.manager.support.restapi.auth;

import com.codingapi.txlcn.manager.support.restapi.auth.sauth.DefaultSAuthLogic;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Date: 2018/12/28
 *
 * @author ujued
 */
@Component
public class TxManagerAdminAuthLogic extends DefaultSAuthLogic {

    public TxManagerAdminAuthLogic(DefaultTokenStorage tokenStorage) {
        super(tokenStorage);
    }


    @Override
    public List<String> ignoreUrls() {
        return Arrays.asList("/admin/login", "/admin/index*", "/admin/js/*", "/admin/css/*", "/admin/assets/*",
                "/assets/*", "/error", "/manager/refresh", "/provider/*", "/");
    }

}
